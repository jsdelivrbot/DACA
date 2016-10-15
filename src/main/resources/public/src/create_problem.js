import {inject} from 'aurelia-framework';
import {initialize} from 'aurelia-pal-browser';
import {HttpClient, json} from 'aurelia-fetch-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import 'fetch';

initialize();

@inject(HttpClient)
export class CreateProblem {
	
	constructor(httpClient) {
		this.httpClient = httpClient;
	}
	
	create() {
		var name = this.name;
		var description = this.description;
		var tip = this.tip;
		this.error = null;
		this.success = false;
		var self = this;
		this.httpClient.fetch('/problem', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
				'Authorization': 'Token ' + localStorage.getItem('token')
			},
			body: JSON.stringify({
				name: name,
				description: description,
				tip: tip
			})
		})
		.then(function(response) {
			return response.json();
		}, function(response) {
			return response.json();
		})
		.then(function(json) {
			if (json.hasOwnProperty('reason')) {
				self.error = json['reason'];
			} else {
				self.success = true;
			}
		});
	}
	
}