/* ==========================================================
   RailBook Pro - Authentication (LocalStorage session)
   ========================================================== */
(function (global) {
    'use strict';

    const SESSION_KEY = 'railbookpro_session';
    const REMEMBER_KEY = 'railbookpro_remember';

    function getSession() {
        const raw = localStorage.getItem(SESSION_KEY) || sessionStorage.getItem(SESSION_KEY);
        if (!raw) return null;
        try { return JSON.parse(raw); } catch (e) { return null; }
    }

    function isLoggedIn() { return !!getSession(); }

    function currentUser() {
        const s = getSession();
        if (!s) return null;
        return Store.find('users', s.userId);
    }

    function isAdmin() {
        const u = currentUser();
        return !!u && u.role === 'ADMIN';
    }

    function login(usernameOrEmail, password, remember) {
        const users = Store.all('users');
        const key = String(usernameOrEmail).trim().toLowerCase();
        const user = users.find(function (u) {
            return u.username.toLowerCase() === key || u.email.toLowerCase() === key;
        });
        if (!user) return { ok: false, error: 'User not found' };
        if (user.password !== password) return { ok: false, error: 'Invalid username or password' };
        if (!user.active) return { ok: false, error: 'Account is inactive. Contact admin.' };

        const session = { userId: user.id, username: user.username, role: user.role, fullName: user.fullName, loginAt: Store.now() };
        const store = remember ? localStorage : sessionStorage;
        store.setItem(SESSION_KEY, JSON.stringify(session));
        if (remember) localStorage.setItem(REMEMBER_KEY, user.username);
        else localStorage.removeItem(REMEMBER_KEY);
        return { ok: true, user: user };
    }

    function register(data) {
        const users = Store.all('users');
        if (users.some(function (u) { return u.username.toLowerCase() === data.username.toLowerCase(); })) {
            return { ok: false, error: 'Username already taken' };
        }
        if (users.some(function (u) { return u.email.toLowerCase() === data.email.toLowerCase(); })) {
            return { ok: false, error: 'Email already registered' };
        }
        const user = Store.insert('users', {
            username: data.username,
            fullName: data.fullName,
            email: data.email,
            phone: data.phone,
            password: data.password,
            gender: data.gender || 'OTHER',
            active: true,
            role: 'PASSENGER',
            createdAt: Store.now()
        });
        return { ok: true, user: user };
    }

    function logout() {
        localStorage.removeItem(SESSION_KEY);
        sessionStorage.removeItem(SESSION_KEY);
        window.location.href = resolvePath('login.html');
    }

    function rememberedUsername() { return localStorage.getItem(REMEMBER_KEY) || ''; }

    // Resolve a path correctly whether we're in root or /admin
    function resolvePath(path) {
        const inAdmin = window.location.pathname.indexOf('/admin/') !== -1;
        return (inAdmin ? '../' : '') + path;
    }

    /**
     * Guard a page. opts = { requireAdmin: bool }
     * Redirects to login if not authed, or to dashboard if role mismatch.
     */
    function guard(opts) {
        opts = opts || {};
        if (!isLoggedIn()) {
            window.location.href = resolvePath('login.html');
            return false;
        }
        if (opts.requireAdmin && !isAdmin()) {
            window.location.href = resolvePath('dashboard.html');
            return false;
        }
        return true;
    }

    global.Auth = {
        getSession: getSession,
        isLoggedIn: isLoggedIn,
        currentUser: currentUser,
        isAdmin: isAdmin,
        login: login,
        register: register,
        logout: logout,
        rememberedUsername: rememberedUsername,
        guard: guard,
        resolvePath: resolvePath
    };
})(window);
