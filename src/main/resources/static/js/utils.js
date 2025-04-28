// Utility functions
class Utils {
    static formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
    }

    static formatCurrency(amount) {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
            minimumFractionDigits: 2
        }).format(amount);
    }

    static showAlert(message, type = 'success') {
        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.top = '20px';
        alertDiv.style.right = '20px';
        alertDiv.style.zIndex = '9999';
        alertDiv.style.maxWidth = '400px';
        alertDiv.role = 'alert';
        
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        `;
        
        document.body.appendChild(alertDiv);
        
        // Auto dismiss after 5 seconds
        setTimeout(() => {
            const bsAlert = new bootstrap.Alert(alertDiv);
            bsAlert.close();
        }, 5000);
    }

    static showLoading(element) {
        element.innerHTML = `
            <div class="spinner-container">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
            </div>
        `;
    }

    static hideLoading(element, content = '') {
        element.innerHTML = content;
    }

    static handleApiError(error) {
        console.error('API Error:', error);
        let errorMessage = 'An error occurred while processing your request.';
        
        if (error.response) {
            if (error.response.status === 404) {
                errorMessage = 'The requested resource was not found.';
            } else if (error.response.status === 400) {
                errorMessage = 'Invalid data provided. Please check your input.';
            } else if (error.response.data && error.response.data.message) {
                errorMessage = error.response.data.message;
            }
        } else if (error.message) {
            errorMessage = error.message;
        }
        
        Utils.showAlert(errorMessage, 'danger');
    }
}

export default Utils;