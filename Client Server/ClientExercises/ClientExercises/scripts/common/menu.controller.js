/**
 * MenuController
 * - highlight the clicked menu entry by executing isActive method
 * - works in conjunction with ng-class attribute in index.html
 */
var MenuController = (function () {
    /**
     * constructor
     * @param location: angular location service
     */
    function MenuController(location) {
        /**
         * switch to turn on/off if user clicked menu entry so menu entry can
         * have active class applied
         */
        this.isActive = function (path) {
            if (path === this.location.path() ||
                (this.location.path() === "" && path === "/")) {
                return true;
            }
        };
        this.location = location;
    }
    // static injection
    MenuController.$inject = ["$location"];
    return MenuController;
})();
// Add the controller to the application
app.controller("MenuController", MenuController);
//# sourceMappingURL=menu.controller.js.map