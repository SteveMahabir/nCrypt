/**
 * HomeController
 * - controller for home.html partial
 */
class HomeController {
    label: string;

    constructor() {
        this.label = "Steve Mahabir";
    } 
}
app.controller("HomeController", [HomeController]);