import {inject} from 'aurelia-framework';
import {initialize} from 'aurelia-pal-browser';
import {HttpClient, json} from 'aurelia-fetch-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import 'fetch';
initialize();

@inject(HttpClient)
export class Problem {
	 
	constructor(httpClient) {
		this.httpClient = httpClient;
		var self = this;
		var headers = { };
		if (localStorage.getItem('token')) {
			headers.Authorization = 'Token ' + localStorage.getItem('token');
		}
		this.httpClient.fetch('problem', {
			method: 'get',
			headers: headers
		}) 
		.then(function(response) {
			return response.json();
		}, function(response) {
			return response.json();
		})
		.then(function(json) {
			console.log(json);
			self.problems = json;
		});
	}
	
} 