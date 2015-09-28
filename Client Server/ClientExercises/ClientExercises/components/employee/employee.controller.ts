class EmployeeController {
    // static injection
    static $inject = ["RESTService", "$modal", "$filter"];

    // members
    employees: Employee[];
    status: string;
    selectedRow: number;
    employee: Employee;


    constructor(public restsvc: RESTService,
        public modal: ng.ui.bootstrap.IModalService,
        public filter: ng.IFilterService) {
        this.loadEmployees();
    }

    public loadEmployees(msg?: string) {
        return this.restsvc.callServer("get", "employee")
            .then((response: Employee[]) => {
                this.employees = response;
                if (msg) {
                    this.status = msg + " - Employees Retrieved";
                }
                else {
                    this.status = "Employees Retrieved";
                }
            })
            .catch((error: any) => this.status = "Employees not retrieved code - " + error);
    } // loadEmployees

    public selectRow(row: number, employee: Employee) {
        this.selectedRow = row;
        //setup modals characteristics
        var options: ng.ui.bootstrap.IModalSettings = {
            templateUrl: "components/employee/employeeModal.html",
            controller: EmployeeModalController.Id + " as ctrlr",
            resolve: { modalData: () => { return employee; } }
        }

        // popup the modal
        this.modal.open(options).result
            .then((results: any) => this.processModal(results))
            .catch((error: any) => this.status = error);
    }// selectRow

    processModal(results: any) {
        var msg = "";

        switch (results.operation) {
            case "update":
                return this.restsvc.callServer("put", "employee", results.employee.employeeid,
                    results.employee)
                    .then((response: any) => {
                        if (parseInt(response, 10) === 1) {
                            msg = "Employee " + results.employee.employeeid + " Updated! ";
                            this.loadEmployees(msg);
                        } // if
                    }) // then
                    .catch((error: any) => this.status = "Employee Not Updated! - " + error);
            case "add":
                return this.restsvc.callServer("post", "employee", results.employee.employeeid,
                    results.employee)
                    .then((response: any) => {
                        if (parseInt(response, 10) > 0) {
                            msg = "Employee " + parseInt(response, 10) + " Added! ";
                            this.loadEmployees(msg);
                        } // if
                    })
                    .catch((error: any) => this.status = "Employee Not Added! - " + error);
            case "delete":
                return this.restsvc.callServer("delete", "employee", results.employee.employeeid,
                    results.employee)
                    .then((response: any) => {
                        if (parseInt(response, 10) === 1) {
                            msg = "Employee " + results.employee.employeeid + " Deleted! ";
                            this.loadEmployees(msg);
                        } // if
                    })
                    .catch((error: any) => this.status = "Employee Not Deleted! - " + error);
            case "cancel":
                this.loadEmployees(results.status);
                this.selectedRow = -1;
                break;
        }
        this.status = results;
    }// processModalChanges


    findSelected(col: number, order: any) {
        this.employees = this.filter("orderBy")(this.employees, col, order);
        if (this.employee) { // if we havnt selected one
            for (var i = 0; i < this.employees.length; i++) {
                if (this.employees[i].employeeid === this.employee.employeeid) {
                    this.selectedRow = i;
                } // if
            }// for
        }// if
    }// findSelected

}// class

app.controller("EmployeeController", EmployeeController);