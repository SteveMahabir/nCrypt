class EmployeeModalController {
    //static injection
    static $inject = ["$modalInstance", "modalData"];

    //members
    static Id = "EmployeeModalController";
    modalTitle: string;
    retVal: any;
    todo: string;

    constructor(public modal: ng.ui.bootstrap.IModalServiceInstance, public employee: Employee) {
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

    add() {
        this.retVal.operation = "add";
        this.retVal.employee = this.employee;
        this.modal.close(this.retVal);
    }// add

    update() {
        this.retVal.operation = "update";
        this.retVal.employee = this.employee;
        this.modal.close(this.retVal);
    }// update

    delete() {
        this.retVal.operation = "delete";
        this.retVal.employee = this.employee;
        this.modal.close(this.retVal);
    }// add


    cancel() {
        this.retVal.operation = "cancel";
        if (this.employee) { // we were updating
            this.retVal.status = this.employee.lastname + " not changed!";
        }
        else { // we were adding
            this.retVal.status = "No Employee Entered";
        }
        this.modal.close(this.retVal);
    } // cancel

}

app.controller("EmployeeModalController", EmployeeModalController);