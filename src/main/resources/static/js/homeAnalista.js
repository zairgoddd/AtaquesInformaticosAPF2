// Script para ajustar el padding del contenido principal según el ancho del sidebar
document.addEventListener('DOMContentLoaded', function() {
    function adjustLayout() {
        const sidebar = document.querySelector('.sidebar');
        const mainContent = document.querySelector('.main-content');
        
        if (window.innerWidth >= 768) {
            const sidebarWidth = sidebar.offsetWidth;
            mainContent.style.paddingLeft = sidebarWidth + 'px';
        } else {
            mainContent.style.paddingLeft = '0';
        }
    }
    
    // Ajustar al cargar y al cambiar el tamaño de la ventana
    adjustLayout();
    window.addEventListener('resize', adjustLayout);
});
function toggleSidebar() {
        const sidebar = document.querySelector('.sidebar');
        const backdrop = document.querySelector('.sidebar-backdrop');

        sidebar.classList.toggle('show');
        backdrop.classList.toggle('show');
    }
   

