/**/
var RESTService = (function () {
    function RESTService(http, q) {
        this.http = http;
        this.q = q;
        this.baseUrl = "http://localhost:8080/ServerExercises/restapi/";
    }
    RESTService.prototype.callServer = function (action, url, id, model) {
        var defTask = this.q.defer();
        switch (action) {
            case "get":
                this.http.get(this.baseUrl + url) // IHttpService call returns it's own promise
                    .then(function (success) { return defTask.resolve(success.data); })
                    .catch(function (error) { return defTask.reject("server error"); });
                break; // get
            case "post":
                this.http.post(this.baseUrl + url, model)
                    .then(function (success) { return defTask.resolve(success.data); })
                    .catch(function (error) { return defTask.reject("server error"); });
                break;
            case "put":
                this.http.put(this.baseUrl + url, model)
                    .then(function (success) { return defTask.resolve(success.data); })
                    .catch(function (error) { return defTask.reject("server error"); });
                break; // put
            case "delete":
                this.http.delete(this.baseUrl + url + "/" + id)
                    .then(function (success) { return defTask.resolve(success.data); })
                    .catch(function (error) { return defTask.reject("server error"); });
                break;
        } // switch
        return defTask.promise;
    }; // callserver
    RESTService.$inject = ["$http", "$q"];
    return RESTService;
})();
app.service("RESTService", RESTService);
//# sourceMappingURL=rest.service.js.map