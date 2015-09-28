/**/
class RESTService {
    static $inject = ["$http", "$q"];
    baseUrl: string = "http://localhost:8080/ServerExercises/restapi/";

    constructor(public http: ng.IHttpService, public q: ng.IQService) { }

    public callServer(action: string, url?: string, id?: string, model?: any) {

        var defTask = this.q.defer();

        switch (action) {
            case "get":
                this.http.get(this.baseUrl + url) // IHttpService call returns it's own promise
                    .then((success: any) => defTask.resolve(success.data))
                    .catch((error: any) => defTask.reject("server error"));
                break; // get
            case "post":
                this.http.post(this.baseUrl + url, model)
                    .then((success: any) => defTask.resolve(success.data))
                    .catch((error: any) => defTask.reject("server error"));
                break;
            case "put":
                this.http.put(this.baseUrl + url, model)
                    .then((success: any) => defTask.resolve(success.data))
                    .catch((error: any) => defTask.reject("server error"));
                break; // put
            case "delete":
                this.http.delete(this.baseUrl + url + "/" + id)
                    .then((success: any) => defTask.resolve(success.data))
                    .catch((error: any) => defTask.reject("server error"));
                break;
        }// switch

        return defTask.promise;
    } // callserver
}

app.service("RESTService", RESTService);