var EmployeeModalController = (function () {
    function EmployeeModalController(modal, employee) {
        this.modal = modal;
        this.employee = employee;
        this.employee = employee;
        if (employee) {
            this.modalTitle = "Update Details for Employee " + employee.employeeid;
            this.todo = "update";
        }
        else {
            this.modalTitle = "Add Details for New Employee";
            this.todo = "add";
        }
        this.retVal = { operation: "", retEmployee: employee, status: "" };
    }
    EmployeeModalController.prototype.add = function () {
        this.retVal.operation = "add";
        this.retVal.employee = this.employee;
        this.modal.close(this.retVal);
    }; // add
    EmployeeModalController.prototype.update = function () {
        this.retVal.operation = "update";
        this.retVal.employee = this.employee;
        this.modal.close(this.retVal);
    }; // update
    EmployeeModalController.prototype.delete = function () {
        this.retVal.operation = "delete";
        this.retVal.employee = this.employee;
        this.modal.close(this.retVal);
    }; // add
    EmployeeModalController.prototype.cancel = function () {
        this.retVal.operation = "cancel";
        if (this.employee) {
            this.retVal.status = this.employee.lastname + " not changed!";
        }
        else {
            this.retVal.status = "No Employee Entered";
        }
        this.modal.close(this.retVal);
    }; // cancel
    //static injection
    EmployeeModalController.$inject = ["$modalInstance", "modalData"];
    //members
    EmployeeModalController.Id = "EmployeeModalController";
    return EmployeeModalController;
})();
app.controller("EmployeeModalController", EmployeeModalController);
//# sourceMappingURL=employee.modal.controller.js.map