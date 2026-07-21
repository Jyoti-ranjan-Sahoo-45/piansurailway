/* ==========================================================
   RailBook Pro - Generic Admin CRUD renderer
   Builds a searchable table + add/edit modal for a collection.
   ========================================================== */
(function (global) {
    'use strict';

    var esc = Utils.escapeHtml;

    /**
     * config = {
     *   mount: HTMLElement,
     *   title: string,
     *   collection: string,              // Store collection name
     *   columns: [{ key, label, render? }],
     *   fields: [{ name, label, type, options?, required?, validators?, transform? }],
     *   searchKeys: [string],            // keys to match against search box
     *   toRecord: fn(formValues) -> object to persist (optional),
     *   fromRecord: fn(record) -> formValues (optional),
     *   beforeSave: fn(values, editingId) -> errorString|null (optional),
     *   afterChange: fn() (optional),
     *   canDelete: fn(record) -> bool (optional)
     * }
     */
    function build(config) {
        var mount = config.mount;
        var editingId = null;

        mount.innerHTML =
            '<div class="page-head"><h2>' + esc(config.title) + '</h2>' +
                '<button class="btn btn-primary" id="add-btn"><i class="fa-solid fa-plus"></i> Add New</button></div>' +
            '<div class="card mb-2"><div class="filter-bar">' +
                '<div class="form-group"><label>Search</label><input class="form-control" id="search-box" placeholder="Type to filter..."></div>' +
            '</div></div>' +
            '<div class="card"><div class="table-wrap"><table class="data-table" id="crud-table"><thead><tr>' +
                config.columns.map(function (c) { return '<th>' + esc(c.label) + '</th>'; }).join('') +
                '<th class="text-right">Actions</th></tr></thead><tbody></tbody></table></div><div id="crud-empty"></div></div>' +
            buildModal(config);

        var modal = Utils.$('#crud-modal', mount) || Utils.$('#crud-modal');
        var form = Utils.$('#crud-form');
        var searchBox = Utils.$('#search-box', mount);

        function openModal(record) {
            editingId = record ? record.id : null;
            Utils.$('#modal-title').textContent = (record ? 'Edit ' : 'Add ') + config.title.replace(/s$/, '');
            Utils.clearErrors(form);
            var values = record ? (config.fromRecord ? config.fromRecord(record) : record) : {};
            config.fields.forEach(function (f) {
                var input = form.elements[f.name];
                if (!input) return;
                var val = values[f.name];
                if (input.type === 'checkbox') input.checked = val === undefined ? true : !!val;
                else input.value = (val === undefined || val === null) ? '' : val;
            });
            modal.classList.add('open');
        }

        function closeModal() { modal.classList.remove('open'); editingId = null; }

        function render() {
            var q = (searchBox.value || '').trim().toLowerCase();
            var rows = Store.all(config.collection).filter(function (r) {
                if (!q) return true;
                return (config.searchKeys || []).some(function (k) {
                    var v = config.cellValue ? config.cellValue(r, k) : r[k];
                    return String(v == null ? '' : v).toLowerCase().indexOf(q) !== -1;
                });
            });

            var tbody = Utils.$('#crud-table tbody', mount);
            var empty = Utils.$('#crud-empty', mount);
            if (!rows.length) {
                Utils.$('#crud-table', mount).style.display = 'none';
                empty.innerHTML = '<div class="empty-state"><i class="fa-solid fa-inbox"></i><h3>No records</h3><p>Click "Add New" to create one.</p></div>';
                return;
            }
            Utils.$('#crud-table', mount).style.display = '';
            empty.innerHTML = '';
            tbody.innerHTML = rows.map(function (r) {
                var tds = config.columns.map(function (c) {
                    return '<td>' + (c.render ? c.render(r) : esc(r[c.key])) + '</td>';
                }).join('');
                var delAllowed = config.canDelete ? config.canDelete(r) : true;
                return '<tr>' + tds + '<td class="text-right">' +
                    '<button class="btn btn-outline btn-sm" data-edit="' + r.id + '"><i class="fa-solid fa-pen"></i></button> ' +
                    (delAllowed ? '<button class="btn btn-danger btn-sm" data-del="' + r.id + '"><i class="fa-solid fa-trash"></i></button>' : '') +
                    '</td></tr>';
            }).join('');

            Utils.$all('[data-edit]', tbody).forEach(function (b) {
                b.addEventListener('click', function () { openModal(Store.find(config.collection, b.getAttribute('data-edit'))); });
            });
            Utils.$all('[data-del]', tbody).forEach(function (b) {
                b.addEventListener('click', function () {
                    if (!confirm('Delete this record? This cannot be undone.')) return;
                    Store.remove(config.collection, b.getAttribute('data-del'));
                    Utils.toast('Deleted', config.title.replace(/s$/, '') + ' removed.', 'success');
                    render();
                    if (config.afterChange) config.afterChange();
                });
            });
        }

        form.addEventListener('submit', function (e) {
            e.preventDefault();
            Utils.clearErrors(form);
            var rules = {};
            config.fields.forEach(function (f) {
                if (f.validators) rules[f.name] = f.validators;
                else if (f.required) rules[f.name] = [Utils.Validators.required];
            });
            if (!Utils.validateForm(form, rules)) return;

            var values = {};
            config.fields.forEach(function (f) {
                var input = form.elements[f.name];
                if (!input) return;
                var v = input.type === 'checkbox' ? input.checked : input.value;
                if (f.transform) v = f.transform(v);
                values[f.name] = v;
            });

            if (config.beforeSave) {
                var err = config.beforeSave(values, editingId);
                if (err) { Utils.toast('Cannot save', err, 'error'); return; }
            }

            var record = config.toRecord ? config.toRecord(values, editingId) : values;
            if (editingId) { Store.update(config.collection, editingId, record); Utils.toast('Updated', config.title.replace(/s$/, '') + ' saved.', 'success'); }
            else { Store.insert(config.collection, record); Utils.toast('Created', 'New ' + config.title.replace(/s$/, '').toLowerCase() + ' added.', 'success'); }

            closeModal();
            render();
            if (config.afterChange) config.afterChange();
        });

        Utils.$('#add-btn', mount).addEventListener('click', function () { openModal(null); });
        searchBox.addEventListener('input', render);
        Utils.$all('[data-close]', modal).forEach(function (b) { b.addEventListener('click', closeModal); });
        modal.addEventListener('click', function (e) { if (e.target === modal) closeModal(); });

        render();
        return { render: render };
    }

    function buildModal(config) {
        var fieldsHtml = config.fields.map(function (f) {
            if (f.type === 'select') {
                var opts = (typeof f.options === 'function' ? f.options() : f.options).map(function (o) {
                    return '<option value="' + esc(o.value) + '">' + esc(o.label) + '</option>';
                }).join('');
                return '<div class="form-group"><label>' + esc(f.label) + '</label><select class="form-control" name="' + f.name + '">' + opts + '</select></div>';
            }
            if (f.type === 'checkbox') {
                return '<div class="form-group"><label><input type="checkbox" name="' + f.name + '"> ' + esc(f.label) + '</label></div>';
            }
            return '<div class="form-group"><label>' + esc(f.label) + '</label>' +
                '<input class="form-control" type="' + (f.type || 'text') + '" name="' + f.name + '"' +
                (f.placeholder ? ' placeholder="' + esc(f.placeholder) + '"' : '') + '></div>';
        }).join('');

        return '<div class="modal-overlay" id="crud-modal"><div class="modal">' +
            '<div class="modal-header"><h3 id="modal-title">Add</h3><button class="modal-close" data-close>&times;</button></div>' +
            '<form id="crud-form"><div class="modal-body">' + fieldsHtml + '</div>' +
            '<div class="modal-footer"><button type="button" class="btn btn-outline" data-close>Cancel</button>' +
            '<button type="submit" class="btn btn-primary">Save</button></div></form>' +
            '</div></div>';
    }

    global.AdminCrud = { build: build };
})(window);
