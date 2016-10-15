import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';

@inject(EventAggregator)
export class App {
	
	constructor(eventAggregator) {
		this.eventAggregator = eventAggregator;
		var self = this;
		this.eventAggregator.subscribe('login', function(token) {
			localStorage.setItem('token', token);
			self.loggedIn = true;
			window.location.href = '#';
		});
		this.loggedIn = localStorage.getItem('token') !== null;
	}
	
	logout() {
		localStorage.clear();
		this.loggedIn = false;
		window.location.href = '#';
	}

	// Implement configureRouter method
	configureRouter(config, router) {
		config.title = 'Dirlididi';
		// Use map to set array of possible routes
		config.map([
			{ route: ['','/home'], name: 'home', moduleId: './home', nav: true, title:'Home' },
			{ route: '/login', name: 'login',  moduleId: './login',    nav: true, title:'Login' },
			{ route: '/problem', name: 'problem', moduleId: './problem', nav: true, title:'Problems' },
			{ route: '/create_problem', name: 'create_problem', moduleId: './create_problem', nav: true, title: 'Create problem' }
		]);
		// Create a binding to the router object
		this.router = router;
	}

}
