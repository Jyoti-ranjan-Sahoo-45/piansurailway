/* ==========================================================
   RailBook Pro - Shared Utilities
   ========================================================== */
(function (global) {
    'use strict';

    // ---------- DOM helpers ----------
    function $(sel, ctx) { return (ctx || document).querySelector(sel); }
    function $all(sel, ctx) { return Array.prototype.slice.call((ctx || document).querySelectorAll(sel)); }

    function el(tag, attrs, children) {
        const node = document.createElement(tag);
        if (attrs) {
            Object.keys(attrs).forEach(function (k) {
                if (k === 'class') node.className = attrs[k];
                else if (k === 'html') node.innerHTML = attrs[k];
                else if (k.indexOf('on') === 0 && typeof attrs[k] === 'function') node.addEventListener(k.slice(2), attrs[k]);
                else node.setAttribute(k, attrs[k]);
            });
        }
        (children || []).forEach(function (c) {
            if (typeof c === 'string') node.appendChild(document.createTextNode(c));
            else if (c) node.appendChild(c);
        });
        return node;
    }

    function escapeHtml(str) {
        if (str === null || str === undefined) return '';
        return String(str)
            .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }

    // ---------- Toast / notifications ----------
    function ensureToastContainer() {
        let c = $('#toast-container');
        if (!c) {
            c = el('div', { id: 'toast-container' });
            document.body.appendChild(c);
        }
        return c;
    }

    function toast(title, message, type) {
        type = type || 'info';
        const icons = { success: 'fa-circle-check', error: 'fa-circle-xmark', info: 'fa-circle-info' };
        const container = ensureToastContainer();
        const node = el('div', { class: 'toast ' + type }, [
            el('i', { class: 'fa-solid ' + (icons[type] || icons.info) + ' toast-icon' }),
            el('div', { class: 'toast-body' }, [
                el('div', { class: 'toast-title' }, [title]),
                message ? el('div', { class: 'toast-msg' }, [message]) : null
            ])
        ]);
        container.appendChild(node);
        setTimeout(function () {
            node.style.transition = 'opacity 0.3s, transform 0.3s';
            node.style.opacity = '0';
            node.style.transform = 'translateX(120%)';
            setTimeout(function () { node.remove(); }, 300);
        }, 3200);
    }

    // ---------- Formatting ----------
    function formatCurrency(amount) {
        return '₹' + Number(amount || 0).toLocaleString('en-IN', { maximumFractionDigits: 0 });
    }

    function formatDate(iso) {
        if (!iso) return '-';
        const d = new Date(iso.length <= 10 ? iso + 'T00:00:00' : iso);
        if (isNaN(d)) return iso;
        return d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
    }

    function formatDateTime(iso) {
        if (!iso) return '-';
        const d = new Date(iso);
        if (isNaN(d)) return iso;
        return d.toLocaleString('en-IN', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' });
    }

    function timeAgo(iso) {
        const d = new Date(iso);
        const secs = Math.floor((Date.now() - d.getTime()) / 1000);
        if (secs < 60) return 'just now';
        const mins = Math.floor(secs / 60);
        if (mins < 60) return mins + 'm ago';
        const hrs = Math.floor(mins / 60);
        if (hrs < 24) return hrs + 'h ago';
        const days = Math.floor(hrs / 24);
        if (days < 30) return days + 'd ago';
        return formatDate(iso);
    }

    // ---------- Validation ----------
    const Validators = {
        required: function (v) { return (v !== null && v !== undefined && String(v).trim() !== '') ? '' : 'This field is required'; },
        email: function (v) { return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v) ? '' : 'Enter a valid email address'; },
        phone: function (v) { return /^[0-9]{10}$/.test(v) ? '' : 'Phone must be 10 digits'; },
        password: function (v) { return String(v).length >= 6 ? '' : 'Password must be at least 6 characters'; },
        trainNumber: function (v) { return /^[0-9]{4,6}$/.test(v) ? '' : 'Train number must be 4-6 digits'; },
        positiveInt: function (v) { return (Number.isInteger(Number(v)) && Number(v) > 0) ? '' : 'Enter a positive number'; },
        futureDate: function (v) {
            if (!v) return 'Date is required';
            const today = new Date(); today.setHours(0, 0, 0, 0);
            return new Date(v + 'T00:00:00') >= today ? '' : 'Date cannot be in the past';
        }
    };

    /**
     * Validate a form. rules = { fieldName: [validatorFn, ...] }
     * Shows errors in a sibling .field-error element. Returns true if valid.
     */
    function validateForm(form, rules) {
        let valid = true;
        Object.keys(rules).forEach(function (name) {
            const input = form.elements[name];
            if (!input) return;
            const value = input.type === 'checkbox' ? input.checked : input.value;
            let error = '';
            for (let i = 0; i < rules[name].length; i++) {
                error = rules[name][i](value);
                if (error) break;
            }
            setFieldError(input, error);
            if (error) valid = false;
        });
        return valid;
    }

    function setFieldError(input, error) {
        let errEl = input.parentNode.querySelector('.field-error');
        if (!errEl) {
            errEl = el('span', { class: 'field-error' });
            input.parentNode.appendChild(errEl);
        }
        errEl.textContent = error || '';
        input.style.borderColor = error ? 'var(--danger)' : '';
    }

    function clearErrors(form) {
        $all('.field-error', form).forEach(function (e) { e.textContent = ''; });
        $all('.form-control', form).forEach(function (i) { i.style.borderColor = ''; });
    }

    // ---------- Query string ----------
    function queryParam(name) {
        return new URLSearchParams(window.location.search).get(name);
    }

    global.Utils = {
        $: $, $all: $all, el: el, escapeHtml: escapeHtml,
        toast: toast,
        formatCurrency: formatCurrency, formatDate: formatDate, formatDateTime: formatDateTime, timeAgo: timeAgo,
        Validators: Validators, validateForm: validateForm, setFieldError: setFieldError, clearErrors: clearErrors,
        queryParam: queryParam
    };
})(window);
