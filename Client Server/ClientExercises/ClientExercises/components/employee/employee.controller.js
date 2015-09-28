var EmployeeController = (function () {
    function EmployeeController(restsvc, modal, filter) {
        this.restsvc = restsvc;
        this.modal = modal;
        this.filter = filter;
        this.loadEmployees();
    }
    EmployeeController.prototype.loadEmployees = function (msg) {
        var _this = this;
        return this.restsvc.callServer("get", "employee")
            .then(function (response) {
            _this.employees = response;
            if (msg) {
                _this.status = msg + " - Employees Retrieved";
            }
            else {
                _this.status = "Employees Retrieved";
            }
        })
            .catch(function (error) { return _this.status = "Employees not retrieved code - " + error; });
    }; // loadEmployees
    EmployeeController.prototype.selectRow = function (row, employee) {
        var _this = this;
        this.selectedRow = row;
        //setup modals characteristics
        var options = {
            templateUrl: "components/employee/employeeModal.html",
            controller: EmployeeModalController.Id + " as ctrlr",
            resolve: { modalData: function () { return employee; } }
        };
        // popup the modal
        this.modal.open(options).result
            .then(function (results) { return _this.processModal(results); })
            .catch(function (error) { return _this.status = error; });
    }; // selectRow
    EmployeeController.prototype.processModal = function (results) {
        var _this = this;
        var msg = "";
        switch (results.operation) {
            case "update":
                return this.restsvc.callServer("put", "employee", results.employee.employeeid, results.employee)
                    .then(function (response) {
                    if (parseInt(response, 10) === 1) {
                        msg = "Employee " + results.employee.employeeid + " Updated! ";
                        _this.loadEmployees(msg);
                    } // if
                }) // then
                    .catch(function (error) { return _this.status = "Employee Not Updated! - " + error; });
            case "add":
                return this.restsvc.callServer("post", "employee", results.employee.employeeid, results.employee)
                    .then(function (response) {
                    if (parseInt(response, 10) > 0) {
                        msg = "Employee " + parseInt(response, 10) + " Added! ";
                        _this.loadEmployees(msg);
                    } // if
                })
                    .catch(function (error) { return _this.status = "Employee Not Added! - " + error; });
            case "delete":
                return this.restsvc.callServer("delete", "employee", results.employee.employeeid, results.employee)
                    .then(function (response) {
                    if (parseInt(response, 10) === 1) {
                        msg = "Employee " + results.employee.employeeid + " Deleted! ";
                        _this.loadEmployees(msg);
                    } // if
                })
                    .catch(function (error) { return _this.status = "Employee Not Deleted! - " + error; });
            case "cancel":
                this.loadEmployees(results.status);
                this.selectedRow = -1;
                break;
        }
        this.status = results;
    }; // processModalChanges
    EmployeeController.prototype.findSelected = function (col, order) {
        this.employees = this.filter("orderBy")(this.employees, col, order);
        if (this.employee) {
            for (var i = 0; i < this.employees.length; i++) {
                if (this.employees[i].employeeid === this.employee.employeeid) {
                    this.selectedRow = i;
                } // if
            } // for
        } // if
    }; // findSelected
    // static injection
    EmployeeController.$inject = ["RESTService", "$modal", "$filter"];
    return EmployeeController;
})(); // class
app.controller("EmployeeController", EmployeeController);
//# sourceMappingURL=employee.controller.js.map