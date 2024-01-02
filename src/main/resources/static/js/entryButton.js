window.addEventListener('DOMContentLoaded', function () {
    const checkbox = document.getElementById('checkbox');
    const submitButton = document.querySelector('.submit-button');

    checkbox.addEventListener('change', function () {
        if (this.checked) {
            submitButton.style.display = 'block';
        } else {
            submitButton.style.display = 'none';
        }
    });
});