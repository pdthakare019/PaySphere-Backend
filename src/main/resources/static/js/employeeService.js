import Utils from './utils.js';

class EmployeeService {
    static BASE_URL = 'http://localhost:8080/api/employees';

    static async getAllEmployees() {
        try {
            const response = await fetch(this.BASE_URL);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async getEmployeeById(id) {
        try {
            const response = await fetch(`${this.BASE_URL}/${id}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async createEmployee(employee) {
        try {
            const response = await fetch(this.BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(employee)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async updateEmployee(id, employee) {
        try {
            const response = await fetch(`${this.BASE_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(employee)
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async deleteEmployee(id) {
        try {
            const response = await fetch(`${this.BASE_URL}/${id}`, {
                method: 'DELETE'
            });
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return true;
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async calculatePayroll() {
        try {
            const response = await fetch(`${this.BASE_URL}/payroll`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async getAverageSalaryByDepartment(departmentName) {
        try {
            const response = await fetch(`${this.BASE_URL}/department/${departmentName}/average-salary`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async getEmployeesGroupedByDepartment() {
        try {
            const response = await fetch(`${this.BASE_URL}/grouped-by-department`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async getTopNHighestPaidEmployees(n) {
        try {
            const response = await fetch(`${this.BASE_URL}/top-salaries/${n}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async calculatePayrollByJobTitle(role) {
        try {
            const response = await fetch(`${this.BASE_URL}/payroll/job-title/${role}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }

    static async findEmployeesHiredInLastNMonths(months) {
        try {
            const response = await fetch(`${this.BASE_URL}/hired-in-last/${months}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            Utils.handleApiError(error);
            throw error;
        }
    }
}

export default EmployeeService;