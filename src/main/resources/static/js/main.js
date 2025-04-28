import EmployeeService from './employeeService.js';
import Utils from './utils.js';

document.addEventListener('DOMContentLoaded', function() {
    // Initialize the application
    initApp();
});

function initApp() {
    // Set up event listeners
    setupEventListeners();
    
    // Load the dashboard view by default
    showDashboardView();
}

function setupEventListeners() {
    // Navigation tabs
    document.getElementById('dashboard-tab').addEventListener('click', showDashboardView);
    document.getElementById('employees-tab').addEventListener('click', showEmployeesView);
    document.getElementById('payroll-tab').addEventListener('click', showPayrollView);
    
    // Report dropdown items
    document.getElementById('department-report').addEventListener('click', () => showReportView('department'));
    document.getElementById('job-title-report').addEventListener('click', () => showReportView('job-title'));
    document.getElementById('hiring-report').addEventListener('click', () => showReportView('hiring'));
    
    // Add employee button
    document.getElementById('add-employee-btn').addEventListener('click', showAddEmployeeModal);
    
    // Employee form submission
    document.getElementById('saveEmployeeBtn').addEventListener('click', saveEmployee);
    
    // Employee search
    document.getElementById('search-btn').addEventListener('click', searchEmployees);
    document.getElementById('employee-search').addEventListener('keyup', function(event) {
        if (event.key === 'Enter') {
            searchEmployees();
        }
    });
    
    // Payroll period change
    document.getElementById('payroll-period').addEventListener('change', updatePayrollPeriod);
}

// View management functions
function showView(viewId) {
    // Hide all views
    document.getElementById('dashboard-view').style.display = 'none';
    document.getElementById('employees-view').style.display = 'none';
    document.getElementById('payroll-view').style.display = 'none';
    document.getElementById('reports-view').style.display = 'none';
    
    // Show the selected view
    document.getElementById(viewId).style.display = 'block';
    
    // Update active tab
    const tabs = document.querySelectorAll('.nav-link');
    tabs.forEach(tab => tab.classList.remove('active'));
    document.querySelector(`[id="${viewId}-tab"]`).classList.add('active');
}

async function showDashboardView() {
    showView('dashboard-view');
    
    try {
        // Load dashboard data
        const [employees, payroll, recentHires, topEarners, groupedEmployees] = await Promise.all([
            EmployeeService.getAllEmployees(),
            EmployeeService.calculatePayroll(),
            EmployeeService.findEmployeesHiredInLastNMonths(3),
            EmployeeService.getTopNHighestPaidEmployees(5),
            EmployeeService.getEmployeesGroupedByDepartment()
        ]);
        
        // Update dashboard metrics
        document.getElementById('total-employees').textContent = employees.length;
        document.getElementById('total-payroll').textContent = Utils.formatCurrency(payroll);
        document.getElementById('recent-hires').textContent = recentHires.length;
        
        // Populate top earners table
        const topEarnersTable = document.getElementById('top-earners-table').getElementsByTagName('tbody')[0];
        topEarnersTable.innerHTML = '';
        
        topEarners.forEach(employee => {
            const row = topEarnersTable.insertRow();
            row.innerHTML = `
                <td>${employee.name}</td>
                <td>${employee.role}</td>
                <td>${Utils.formatCurrency(employee.salary)}</td>
            `;
        });
        
        // Create department chart
        createDepartmentChart(groupedEmployees);
        
    } catch (error) {
        console.error('Error loading dashboard data:', error);
    }
}

async function showEmployeesView(page = 1, searchTerm = '') {
    showView('employees-view');
    
    try {
        const employees = await EmployeeService.getAllEmployees();
        
        // Filter employees if search term exists
        let filteredEmployees = employees;
        if (searchTerm) {
            const term = searchTerm.toLowerCase();
            filteredEmployees = employees.filter(emp => 
                emp.name.toLowerCase().includes(term) || 
                emp.role.toLowerCase().includes(term) ||
                emp.department.toLowerCase().includes(term)
            );
        }
        
        // Pagination
        const itemsPerPage = 10;
        const totalPages = Math.ceil(filteredEmployees.length / itemsPerPage);
        const paginatedEmployees = filteredEmployees.slice(
            (page - 1) * itemsPerPage,
            page * itemsPerPage
        );
        
        // Populate employees table
        const employeesTable = document.getElementById('employees-table').getElementsByTagName('tbody')[0];
        employeesTable.innerHTML = '';
        
        paginatedEmployees.forEach(employee => {
            const row = employeesTable.insertRow();
            row.innerHTML = `
                <td>${employee.id}</td>
                <td>${employee.name}</td>
                <td>${employee.role}</td>
                <td>${employee.department}</td>
                <td>${Utils.formatCurrency(employee.salary)}</td>
                <td>${Utils.formatDate(employee.hiringDate)}</td>
                <td>
                    <button class="btn btn-sm btn-primary me-1 edit-btn" data-id="${employee.id}">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger delete-btn" data-id="${employee.id}">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            `;
        });
        
        // Set up pagination
        setupPagination(page, totalPages, searchTerm);
        
        // Add event listeners to edit and delete buttons
        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', () => showEditEmployeeModal(btn.dataset.id));
        });
        
        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', () => confirmDeleteEmployee(btn.dataset.id));
        });
        
    } catch (error) {
        console.error('Error loading employees:', error);
    }
}

async function showPayrollView() {
    showView('payroll-view');
    
    try {
        const payroll = await EmployeeService.calculatePayroll();
        const groupedEmployees = await EmployeeService.getEmployeesGroupedByDepartment();
        
        // Update payroll summary
        const baseSalaries = payroll * 0.9; // Assuming 90% is base salary
        const bonuses = payroll * 0.1; // Assuming 10% is bonuses
        document.getElementById('base-salaries').textContent = Utils.formatCurrency(baseSalaries);
        document.getElementById('bonuses').textContent = Utils.formatCurrency(bonuses);
        document.getElementById('payroll-total').textContent = Utils.formatCurrency(payroll);
        
        // Create payroll by role chart
        createPayrollByRoleChart();
        
        // Create payroll by department chart and table
        createPayrollByDepartmentChart(groupedEmployees);
        
    } catch (error) {
        console.error('Error loading payroll data:', error);
    }
}

async function showReportView(reportType) {
    showView('reports-view');
    
    const reportContent = document.getElementById('report-content');
    Utils.showLoading(reportContent);
    
    try {
        let content = '';
        let title = '';
        
        switch (reportType) {
            case 'department':
                title = 'Department Analysis';
                const groupedEmployees = await EmployeeService.getEmployeesGroupedByDepartment();
                content = generateDepartmentReport(groupedEmployees);
                break;
                
            case 'job-title':
                title = 'Job Title Analysis';
                const employees = await EmployeeService.getAllEmployees();
                content = generateJobTitleReport(employees);
                break;
                
            case 'hiring':
                title = 'Hiring Trends';
                const recentHires = await EmployeeService.findEmployeesHiredInLastNMonths(12);
                content = generateHiringReport(recentHires);
                break;
        }
        
        document.getElementById('report-title').textContent = title;
        Utils.hideLoading(reportContent, content);
        
    } catch (error) {
        console.error('Error generating report:', error);
        Utils.hideLoading(reportContent, '<p class="text-danger">Error loading report data.</p>');
    }
}

// Employee CRUD operations
function showAddEmployeeModal() {
    const modal = new bootstrap.Modal(document.getElementById('employeeModal'));
    document.getElementById('modalTitle').textContent = 'Add New Employee';
    document.getElementById('employeeForm').reset();
    document.getElementById('employeeId').value = '';
    modal.show();
}

async function showEditEmployeeModal(id) {
    try {
        const employee = await EmployeeService.getEmployeeById(id);
        
        const modal = new bootstrap.Modal(document.getElementById('employeeModal'));
        document.getElementById('modalTitle').textContent = 'Edit Employee';
        document.getElementById('employeeId').value = employee.id;
        document.getElementById('name').value = employee.name;
        document.getElementById('role').value = employee.role;
        document.getElementById('department').value = employee.department;
        document.getElementById('salary').value = employee.salary;
        document.getElementById('hiringDate').value = employee.hiringDate.split('T')[0];
        
        modal.show();
    } catch (error) {
        console.error('Error loading employee for edit:', error);
    }
}

async function saveEmployee() {
    const form = document.getElementById('employeeForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }
    
    const employee = {
        name: document.getElementById('name').value,
        role: document.getElementById('role').value,
        department: document.getElementById('department').value,
        salary: parseFloat(document.getElementById('salary').value),
        hiringDate: document.getElementById('hiringDate').value
    };
    
    const id = document.getElementById('employeeId').value;
    const modal = bootstrap.Modal.getInstance(document.getElementById('employeeModal'));
    
    try {
        if (id) {
            // Update existing employee
            await EmployeeService.updateEmployee(id, employee);
            Utils.showAlert('Employee updated successfully!');
        } else {
            // Create new employee
            await EmployeeService.createEmployee(employee);
            Utils.showAlert('Employee added successfully!');
        }
        
        modal.hide();
        
        // Refresh the current view
        if (document.getElementById('employees-view').style.display !== 'none') {
            showEmployeesView();
        } else {
            showDashboardView();
        }
        
    } catch (error) {
        console.error('Error saving employee:', error);
    }
}

function confirmDeleteEmployee(id) {
    const confirmModal = new bootstrap.Modal(document.getElementById('confirmModal'));
    document.getElementById('confirmMessage').textContent = 'Are you sure you want to delete this employee?';
    
    document.getElementById('confirmActionBtn').onclick = async function() {
        try {
            await EmployeeService.deleteEmployee(id);
            Utils.showAlert('Employee deleted successfully!');
            confirmModal.hide();
            showEmployeesView();
        } catch (error) {
            console.error('Error deleting employee:', error);
            confirmModal.hide();
        }
    };
    
    confirmModal.show();
}

// Search and pagination
function searchEmployees() {
    const searchTerm = document.getElementById('employee-search').value.trim();
    showEmployeesView(1, searchTerm);
}

function setupPagination(currentPage, totalPages, searchTerm = '') {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';
    
    // Previous button
    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    prevLi.innerHTML = `<a class="page-link" href="#">Previous</a>`;
    prevLi.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentPage > 1) showEmployeesView(currentPage - 1, searchTerm);
    });
    pagination.appendChild(prevLi);
    
    // Page numbers
    for (let i = 1; i <= totalPages; i++) {
        const pageLi = document.createElement('li');
        pageLi.className = `page-item ${i === currentPage ? 'active' : ''}`;
        pageLi.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        pageLi.addEventListener('click', (e) => {
            e.preventDefault();
            showEmployeesView(i, searchTerm);
        });
        pagination.appendChild(pageLi);
    }
    
    // Next button
    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    nextLi.innerHTML = `<a class="page-link" href="#">Next</a>`;
}