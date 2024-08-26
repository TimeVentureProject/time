// navigation.js
document.addEventListener('DOMContentLoaded', () => {
    const menuItems = document.querySelectorAll('.navi-menu-item');
    const contentContainer = document.querySelector('.content-container');

    menuItems.forEach(item => {
        item.addEventListener('click', () => {
            menuItems.forEach(item => item.classList.remove('active'));
            item.classList.add('active');
            const target = item.getAttribute('data-target');
            const target = item.getAttribute('data-target');
            axios.get(target)
                .then(response => {
                    contentContainer.innerHTML = response.data;
                    const appElement = contentContainer.querySelector('[data-app]');
                    if (appElement) {
                        const appName = appElement.getAttribute('data-app');
                        new Vue({
                            el: appElement,
                            data: {
                                project: projectData
                            }
                        });
                    }
                })
                .catch(error => {
                    console.error('Error loading content:', error);
                });
        });
    });
});