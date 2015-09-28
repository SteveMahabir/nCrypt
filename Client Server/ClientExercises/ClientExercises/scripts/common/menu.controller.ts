/**
 * MenuController
 * - highlight the clicked menu entry by executing isActive method
 * - works in conjunction with ng-class attribute in index.html
 */
class MenuController {
    // static injection
    static $inject = ["$location"];

    // members
    location: ng.ILocationService;

    /**
     * constructor
     * @param location: angular location service  
     */
    constructor(location: ng.ILocationService) {
        this.location = location;
    }

    /**
     * switch to turn on/off if user clicked menu entry so menu entry can
     * have active class applied
     */
    public isActive = function (path) {
        if (path === this.location.path() ||
            (this.location.path() === "" && path === "/")) {
            return true;
        }
    };
}
// Add the controller to the application
app.controller("MenuController", MenuController);