/**
 * RoutesController
 * - uses new Angular Router format - router-e5.js
 * - utilizes components (html + controller)
 */
var RoutesController = (function () {
    /**
     * constructor
     * @param router: angular's new router service
     */
    function RoutesController($router) {
        this.$router = $router;
        $router.config([
            { path: '/', redirectTo: '/home' },
            { path: '/home', component: 'home' },
            { path: '/employee', component: 'employee' }
        ]);
    }
    // static injection
    RoutesController.$inject = ["$router"];
    return RoutesController;
})();
// Add the controller to the application
app.controller("RoutesController", RoutesController);
//# sourceMappingURL=routes.controller.js.map