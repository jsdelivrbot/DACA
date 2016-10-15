import {inject} from 'aurelia-framework';
import {initialize} from 'aurelia-pal-browser';
import {HttpClient, json} from 'aurelia-fetch-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import 'fetch';
initialize();

@inject(EventAggregator, HttpClient)
export class Login {
	
	constructor(eventAggregator, httpClient) {
		this.eventAggregator = eventAggregator;
		this.httpClient = httpClient;
	}
	 
	login() {
		var email = this.loginEmail;
		var password = this.loginPassword;
		var self = this;
		this.httpClient.fetch('/auth', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				email: email,
				password: password
			})
		})
		.then(function(response) {
			return response.json();
		}, function(response) {
			return response.json();
		})
		.then(function(json) {
			if (json.hasOwnProperty('token')) {
				self.eventAggregator.publish('login', json['token'], email);
			} else {
				self.loginError = json['reason'];
			}
		});
	}
	
	signup() {
		var email = this.signupEmail;
		var password = this.signupPassword;
		this.signupError = false;
		var self = this;
		this.httpClient.fetch('/user', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				email: email,
				password: password
			})
		})
		.then(function(response) {
			return response.json();
		}, function(response) {
			return response.json();
		})
		.then(function(json) {
			if (json.hasOwnProperty('token')) {
				self.eventAggregator.publish('login', json['token'], email);
			} else {
				self.signupError = json['reason'];
			}
		});
	}
	
} 