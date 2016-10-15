export class App {
  // Implement configureRouter method
  configureRouter(config, router) {
    config.title = 'Dirlididi';
    // Use map to set array of possible routes
    config.map([
      { route: ['','welcome'], name: 'welcome', moduleId: './welcome', nav: true, title:'Welcome' },
      { route: 'problems', name: 'problems',  moduleId: './problems',    nav: true, title:'Problems' },
      { route: ['edit','edit/:id'], name: 'edit',  moduleId: './edit',    nav: true, title:'Edit problem', href:'#/edit' }
    ]);

    // Create a binding to the router object
    this.router = router;
  }
}
