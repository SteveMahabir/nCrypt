/**
 * RoutesController
 * - uses new Angular Router format - router-e5.js
 * - utilizes components (html + controller)
 */
class RoutesController {
    // static injection
    static $inject = ["$router"];  

    /**
     * constructor
     * @param router: angular's new router service
     */
    constructor(private $router) {
        $router.config([
            { path: '/', redirectTo: '/home' },
            { path: '/home', component: 'home' },
            { path: '/employee', component: 'employee' }
        ]);
    }
}
// Add the controller to the application
app.controller("RoutesController", RoutesController);