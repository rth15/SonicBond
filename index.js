#!/usr/bin/env node

/**
  SonarAlarmServer
  author: Kechi Weir (kw16d)
  date: 03/29/2018
  
  This is the Server module for the Sonar Attack Alert application

 Server REST API
---------------

GET  /get-alerts[?=since] [p]
POST /get-alerts params: [since]

returns a list of alerts, after the optional id given with since. Example (since=11):

[
    {
        "lat": "31.146892",
        "lon": -83.70274,
        "location": "Colquitt County",
        "volume": 0.5,
        "lan": null,
        "id": 12
    },
    {
        "lat": "31.146892",
        "lon": -83.70274,
        "location": "Colquitt County",
        "volume": 0.5,
        "lan": null,
        "id": 13
    },
    {
        "lat": "31.146892",
        "lon": -83.70274,
        "location": "Colquitt County",
        "volume": 0.5,
        "lan": null,
        "id": 14
    }
]


POST /new-alert

params:
lat: latitude Number [-90, 90]
lon: longitude Number [-180, 180]
location: location string [at least 2 in length]
volume: how strong is the alert [0.-10.]

returns {id: the id of the inserted alert}

Example:

{
    "id": 15
}

 */

const express = require('express');
const util = require('util');
const winston = require('winston');
const bodyParser = require('body-parser');
const fs = require('fs');

var logger = new(winston.Logger)({
	exitOnError: false,
	transports: [
		new(winston.transports.Console)({ timestamp: true, level: 'info', colorize: true }),
	]
});


var PORT = process.env.PORT || 9220;


/**
 * if the latitude given is invalid
 * an error is returned, else an empty string is returned
 * @param {*} val the value to be check
 */
function validateLat(val) {
	if(typeof val == 'undefined')
		return "lat is undefined";
	if(!(val >= -90 && val <= 90))
		return "lat value is out of range (-90, 90)";
	return "";
}

/**
 * if the longitude given is invalid
 * an error is returned, else an empty string is returned
 * @param {*} val the value to be check
 */
function validateLon(val) {
	if(typeof val == 'undefined')
		return "lon is undefined";
	if(!(val >= -180 && val <= 180))
		return "lon value is out of range (-180, 180)";
	return "";
}

/**
 * if the volume given is invalid
 * an error is returned, else an empty string is returned
 * @param {*} val the value to be check
 */
function validateVolume(val) {
	if(typeof val == 'undefined')
		return "volume is undefined";
	if(!(val >= 0. && val <= 10.))
		return "volume value is out of range (0, 10)";
	return "";
}

/**
 * if the location given is invalid
 * an error is returned, else an empty string is returned
 * @param {*} val the value to be check
 */
function validateLocation(val) {
	if(typeof val == 'undefined')
		return "location is undefined";
	if((""+val).length < 2)
		return "location is too short (need to be at least 2)";
	if((""+val).length > 40)
		return "location is too long (need to be at most 40)";
	return "";
}


/**
 * Constructor for server
 */
function SonarAlarmServer() {
	this.port = PORT;
	this.alerts = [];
	this.loadAlertsFromFile();
	this.counter = 0;

	this.app = express();
	this.app.use(bodyParser.urlencoded({ extended: true }));
	this.app.use(bodyParser.json());
	this.setupEndpoints();
}

/**
 * Setup step for endpoints, broken down here
 * to make the code more readable
 */
SonarAlarmServer.prototype.setupEndpoints = function() {
	var self = this;
	self.app.all('/get-alerts', function (req, res) {
		var options = {};
		if(typeof req.query.since !== 'undefined')
			options['since'] = req.query.since;
		if(typeof req.body.since !== 'undefined')
			options['since'] = req.body.since;
		logger.info('get-alerts:', util.inspect(options));

		var dataToSend = JSON.stringify(self.getAlerts(options));
		res.send(dataToSend);
	});	

	self.app.post('/new-alert', function (req, res) {
		logger.info('new-alert:', util.inspect(req.body));
		
		var alert = req.body;
		var error = "";
		if((error = validateLat(alert.lat))
		|| (error = validateLon(alert.lon))
		|| (error = validateVolume(alert.volume))
		|| (error = validateLocation(alert.location))) {
			res.send({error: error});
		}
		else {
			alert.lat = Number(alert.lat);
			alert.lon = Number(alert.lon);
			alert.volume = Number(alert.volume);
			alert.location = ""+alert.location;

			var id = self.addNewAlert(alert);
			res.send({id: id});
		}
	});	
}

/**
 * Adds new alert to storage
 * @param {*} alert 
 */
SonarAlarmServer.prototype.addNewAlert = function(alert) {
	this.counter = this.alerts.length;

	alert['id'] = this.counter++;
	this.alerts.push(alert);
	this.saveAlertsToFile();
	return alert['id'];
}


/**
 * Gets alerts from storage
 * @param {*} options 
 */
SonarAlarmServer.prototype.getAlerts = function(options) {
	var last = typeof options.since !== 'undefined'?Number(options.since):-1;
	var ret = this.alerts.slice(last+1);
	return ret;
}

/**
 * Loads alerts from storage to memory
 */
SonarAlarmServer.prototype.loadAlertsFromFile = function(){
	try {
		this.alerts = JSON.parse(fs.readFileSync('alerts.json', 'utf8'));	
		this.counter = this.alerts.length;
	} catch(e) {
		logger.error('Failed to load alerts from file', e);
	}
}

/**
 * Save alerts to file
 */
SonarAlarmServer.prototype.saveAlertsToFile = function(){
	fs.writeFile("alerts.json", JSON.stringify(this.alerts), function(err) {
		if(err)
			return logger.error('Failed to save alerts to file:', err);
	
		logger.info('Alerts saved to file');
	}); 
}

/**
 * Start the server loop
 */
SonarAlarmServer.prototype.start = function() {
	this.app.listen(this.port);
	logger.info('Listering on port '+this.port);
}


//If the script is called directly create a server instance and start listening for requests
if (require.main === module) {
	var server = new SonarAlarmServer();
	server.start();

	process.on('uncaughtException', function (err) {
		logger.error(err);
	});
}

module.exports = SonarAlarmServer;