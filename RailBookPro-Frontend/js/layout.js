/* ==========================================================
   RailBook Pro - App Shell (sidebar + topbar + notifications)
   Injects the layout into pages with a #app-shell container.
   ========================================================== */
(function (global) {
    'use strict';

    const el = Utils.el;
    const escapeHtml = Utils.escapeHtml;

    const PASSENGER_NAV = [
        { section: 'Menu', items: [
            { label: 'Dashboard', icon: 'fa-gauge-high', href: 'dashboard.html' },
            { label: 'Search Trains', icon: 'fa-magnifying-glass', href: 'search.html' },
            { label: 'Book Ticket', icon: 'fa-ticket', href: 'booking.html' },
            { label: 'My Bookings', icon: 'fa-clock-rotate-left', href: 'history.html' }
        ]},
        { section: 'Account', items: [
            { label: 'My Profile', icon: 'fa-user', href: 'profile.html' }
        ]}
    ];

    const ADMIN_NAV = [
        { section: 'Overview', items: [
            { label: 'Dashboard', icon: 'fa-gauge-high', href: 'admin/dashboard.html' }
        ]},
        { section: 'Management', items: [
            { label: 'Users', icon: 'fa-users', href: 'admin/users.html' },
            { label: 'Trains', icon: 'fa-train', href: 'admin/trains.html' },
            { label: 'Stations', icon: 'fa-building', href: 'admin/stations.html' },
            { label: 'Routes', icon: 'fa-route', href: 'admin/routes.html' },
            { label: 'Schedules', icon: 'fa-calendar-days', href: 'admin/schedules.html' },
            { label: 'Bookings', icon: 'fa-ticket', href: 'admin/bookings.html' }
        ]},
        { section: 'Insights', items: [
            { label: 'Reports', icon: 'fa-chart-column', href: 'admin/reports.html' }
        ]}
    ];

    function initials(name) {
        return String(name || '?').split(' ').map(function (p) { return p[0]; }).slice(0, 2).join('').toUpperCase();
    }

    /**
     * Render the shell.
     * opts = { title: string, active: string (href basename), admin: bool }
     */
    function render(opts) {
        opts = opts || {};
        const user = Auth.currentUser();
        const isAdmin = Auth.isAdmin();
        const inAdmin = window.location.pathname.indexOf('/admin/') !== -1;
        const prefix = inAdmin ? '../' : '';
        const nav = (opts.admin || isAdmin) ? ADMIN_NAV : PASSENGER_NAV;

        const shell = Utils.$('#app-shell');
        if (!shell) return;

        // Build nav HTML
        let navHtml = '';
        nav.forEach(function (group) {
            navHtml += '<div class="nav-section"><div class="nav-section-title">' + group.section + '</div>';
            group.items.forEach(function (item) {
                const base = item.href.split('/').pop();
                const active = base === opts.active ? ' active' : '';
                navHtml += '<a class="nav-link' + active + '" href="' + prefix + item.href + '">' +
                    '<i class="fa-solid ' + item.icon + '"></i> ' + item.label + '</a>';
            });
            navHtml += '</div>';
        });

        shell.className = 'app';
        shell.innerHTML =
            '<aside class="sidebar" id="sidebar">' +
                '<div class="sidebar-brand"><i class="fa-solid fa-train-subway logo"></i> RailBook Pro</div>' +
                navHtml +
                '<div class="nav-section" style="margin-top:auto">' +
                    '<a class="nav-link" href="#" id="logout-link"><i class="fa-solid fa-right-from-bracket"></i> Logout</a>' +
                '</div>' +
            '</aside>' +
            '<div class="main">' +
                '<header class="topbar">' +
                    '<div style="display:flex;align-items:center;gap:14px">' +
                        '<button class="hamburger" id="hamburger"><i class="fa-solid fa-bars"></i></button>' +
                        '<h2>' + escapeHtml(opts.title || 'Dashboard') + '</h2>' +
                    '</div>' +
                    '<div class="topbar-right">' +
                        '<div class="notif-bell" id="notif-bell"><i class="fa-solid fa-bell"></i>' +
                            '<span class="notif-badge" id="notif-badge" style="display:none">0</span></div>' +
                        '<div class="user-chip">' +
                            '<div class="avatar">' + initials(user ? user.fullName : '') + '</div>' +
                            '<div class="meta"><div>' + escapeHtml(user ? user.fullName : '') + '</div>' +
                                '<small>' + (isAdmin ? 'Administrator' : 'Passenger') + '</small></div>' +
                        '</div>' +
                    '</div>' +
                '</header>' +
                '<div class="notif-panel" id="notif-panel"></div>' +
                '<main class="content" id="page-content"></main>' +
            '</div>';

        // Wire up events
        Utils.$('#logout-link').addEventListener('click', function (e) { e.preventDefault(); Auth.logout(); });
        Utils.$('#hamburger').addEventListener('click', function () {
            Utils.$('#sidebar').classList.toggle('open');
        });

        setupNotifications();
        return Utils.$('#page-content');
    }

    function setupNotifications() {
        const bell = Utils.$('#notif-bell');
        const panel = Utils.$('#notif-panel');
        const badge = Utils.$('#notif-badge');

        function refreshBadge() {
            const count = Api.unreadCount();
            if (count > 0) { badge.style.display = ''; badge.textContent = count; }
            else badge.style.display = 'none';
        }

        function renderPanel() {
            const items = Api.myNotifications();
            let html = '<div class="notif-head">Notifications</div>';
            if (!items.length) {
                html += '<div class="notif-empty"><i class="fa-regular fa-bell-slash"></i><br>No notifications</div>';
            } else {
                items.forEach(function (n) {
                    html += '<div class="notif-item' + (n.readFlag ? '' : ' unread') + '">' +
                        '<div class="n-title">' + escapeHtml(n.title) + '</div>' +
                        '<div class="n-msg">' + escapeHtml(n.message) + '</div>' +
                        '<div class="n-time">' + Utils.timeAgo(n.createdAt) + '</div></div>';
                });
            }
            panel.innerHTML = html;
        }

        bell.addEventListener('click', function (e) {
            e.stopPropagation();
            const opening = !panel.classList.contains('open');
            if (opening) { renderPanel(); panel.classList.add('open'); }
            else panel.classList.remove('open');
            if (opening) {
                Api.myNotifications().forEach(function (n) { if (!n.readFlag) Api.markNotificationRead(n.id); });
                setTimeout(refreshBadge, 50);
            }
        });

        document.addEventListener('click', function (e) {
            if (!panel.contains(e.target) && !bell.contains(e.target)) panel.classList.remove('open');
        });

        refreshBadge();
    }

    global.Layout = { render: render };
})(window);
