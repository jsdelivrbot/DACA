import {inject} from 'aurelia-framework';
import {initialize} from 'aurelia-pal-browser';
import {HttpClient, json} from 'aurelia-fetch-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import 'fetch';
initialize();

@inject(EventAggregator, HttpClient)
export class Home {
	   
	updateSolved() {
		if (localStorage.getItem('email')) {
			this.httpClient.fetch('user/' + localStorage.getItem('email') + '/stats', {
				method: 'get'
			}) 
			.then(function(response) {
				return response.json();
			}, function(response) {
				return response.json();
			})
			.then(function(json) {
				self.solved = json.solved;
			});
		}
	}
	
	constructor(eventAggregator, httpClient) {
		this.eventAggregator = eventAggregator;
		this.httpClient = httpClient;
		this.problems = 0;
		this.submitters = 0;
		this.solved = 0;
		console.log('oi');
		var self = this;
		this.httpClient.fetch('stats', {
			method: 'get'
		}) 
		.then(function(response) {
			return response.json();
		}, function(response) {
			return response.json();
		})
		.then(function(json) {
			self.problems = json.problems;
			self.submitters = json.submitters;
		});
		this.updateSolved();
		this.eventAggregator.subscribe('login', function(token, email) {
			self.updateSolved();
		});
	}
	
}