const API_BASE = 'http://localhost:8080/api';

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    initTabs();
    loadServices();
});

// Переключение вкладок
function initTabs() {
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const tabName = btn.dataset.tab;
            switchTab(tabName);
        });
    });
}

function switchTab(tabName) {
    // Убрать активный класс со всех вкладок и контента
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    
    // Добавить активный класс к выбранной вкладке
    document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
    document.getElementById(tabName).classList.add('active');
    
    // Загрузить данные для выбранной вкладки
    switch(tabName) {
        case 'services': loadServices(); break;
        case 'masters': loadMasters(); break;
        case 'appointments': loadAppointments(); break;
        case 'users': loadUsers(); break;
        case 'reports': loadReports(); break;
    }
}

// ========== УСЛУГИ ==========
async function loadServices() {
    try {
        const response = await fetch(`${API_BASE}/services`);
        const services = await response.json();
        displayServices(services);
    } catch (error) {
        document.getElementById('services-list').innerHTML = 
            '<div class="error">Ошибка загрузки услуг: ' + error.message + '</div>';
    }
}

function displayServices(services) {
    const container = document.getElementById('services-list');
    if (services.length === 0) {
        container.innerHTML = '<div class="loading">Нет услуг</div>';
        return;
    }
    
    container.innerHTML = services.map(service => `
        <div class="card">
            <div class="card-header">
                <div class="card-title">${service.name}</div>
                <div class="card-actions">
                    <button class="btn btn-small btn-primary" onclick="editService(${service.id})">✏️</button>
                    <button class="btn btn-small btn-danger" onclick="deleteService(${service.id})">🗑️</button>
                </div>
            </div>
            <div class="card-body">
                <div class="card-field">
                    <span class="card-label">Описание:</span>
                    <span class="card-value">${service.description || 'Нет описания'}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Цена:</span>
                    <span class="card-value">${service.price} ₽</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Длительность:</span>
                    <span class="card-value">${service.duration} мин</span>
                </div>
            </div>
        </div>
    `).join('');
}

let editingServiceId = null;

function showAddServiceForm() {
    editingServiceId = null;
    const form = `
        <h2>Добавить услугу</h2>
        <form onsubmit="saveService(event)">
            <div class="form-group">
                <label>Название:</label>
                <input type="text" name="name" required>
            </div>
            <div class="form-group">
                <label>Описание:</label>
                <textarea name="description" rows="3"></textarea>
            </div>
            <div class="form-group">
                <label>Цена (₽):</label>
                <input type="number" name="price" step="0.01" required>
            </div>
            <div class="form-group">
                <label>Длительность (мин):</label>
                <input type="number" name="duration" required>
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                <button type="submit" class="btn btn-primary">Сохранить</button>
            </div>
        </form>
    `;
    showModal(form);
}

async function editService(id) {
    editingServiceId = id;
    try {
        const response = await fetch(`${API_BASE}/services/${id}`);
        const service = await response.json();
        
        const form = `
            <h2>Редактировать услугу</h2>
            <form onsubmit="saveService(event)">
                <div class="form-group">
                    <label>Название:</label>
                    <input type="text" name="name" value="${service.name || ''}" required>
                </div>
                <div class="form-group">
                    <label>Описание:</label>
                    <textarea name="description" rows="3">${service.description || ''}</textarea>
                </div>
                <div class="form-group">
                    <label>Цена (₽):</label>
                    <input type="number" name="price" step="0.01" value="${service.price || ''}" required>
                </div>
                <div class="form-group">
                    <label>Длительность (мин):</label>
                    <input type="number" name="duration" value="${service.duration || ''}" required>
                </div>
                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                    <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                </div>
            </form>
        `;
        showModal(form);
    } catch (error) {
        alert('Ошибка загрузки услуги: ' + error.message);
    }
}

async function saveService(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const service = {
        name: formData.get('name'),
        description: formData.get('description'),
        price: parseFloat(formData.get('price')),
        duration: parseInt(formData.get('duration'))
    };
    
    try {
        const url = editingServiceId 
            ? `${API_BASE}/services/${editingServiceId}`
            : `${API_BASE}/services`;
        const method = editingServiceId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(service)
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Ошибка сохранения');
        }
        
        closeModal();
        editingServiceId = null;
        loadServices();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function deleteService(id) {
    if (!confirm('Удалить услугу?')) return;
    try {
        await fetch(`${API_BASE}/services/${id}`, { method: 'DELETE' });
        loadServices();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

// ========== МАСТЕРА ==========
async function loadMasters() {
    try {
        const response = await fetch(`${API_BASE}/masters`);
        const masters = await response.json();
        displayMasters(masters);
    } catch (error) {
        document.getElementById('masters-list').innerHTML = 
            '<div class="error">Ошибка загрузки мастеров: ' + error.message + '</div>';
    }
}

let editingMasterId = null;

function displayMasters(masters) {
    const container = document.getElementById('masters-list');
    if (masters.length === 0) {
        container.innerHTML = '<div class="loading">Нет мастеров</div>';
        return;
    }
    
    container.innerHTML = masters.map(master => `
        <div class="card">
            <div class="card-header">
                <div class="card-title">${master.name}</div>
                <div class="card-actions">
                    <button class="btn btn-small btn-primary" onclick="editMaster(${master.id})">✏️</button>
                    <button class="btn btn-small btn-danger" onclick="deleteMaster(${master.id})">🗑️</button>
                </div>
            </div>
            <div class="card-body">
                <div class="card-field">
                    <span class="card-label">Специализация:</span>
                    <span class="card-value">${master.specialization || 'Не указана'}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Стаж:</span>
                    <span class="card-value">${master.experience} лет</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Рейтинг:</span>
                    <span class="card-value">⭐ ${master.rating || 0}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function showAddMasterForm() {
    editingMasterId = null;
    const form = `
        <h2>Добавить мастера</h2>
        <form onsubmit="saveMaster(event)">
            <div class="form-group">
                <label>Имя:</label>
                <input type="text" name="name" required>
            </div>
            <div class="form-group">
                <label>Специализация:</label>
                <input type="text" name="specialization">
            </div>
            <div class="form-group">
                <label>Стаж (лет):</label>
                <input type="number" name="experience" value="0" min="0">
            </div>
            <div class="form-group">
                <label>Рейтинг:</label>
                <input type="number" name="rating" step="0.1" min="0" max="5" value="0">
            </div>
            <div class="form-group">
                <label>ID пользователя:</label>
                <input type="number" name="userId" required>
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                <button type="submit" class="btn btn-primary">Сохранить</button>
            </div>
        </form>
    `;
    showModal(form);
}

async function editMaster(id) {
    editingMasterId = id;
    try {
        const response = await fetch(`${API_BASE}/masters/${id}`);
        const master = await response.json();
        
        const form = `
            <h2>Редактировать мастера</h2>
            <form onsubmit="saveMaster(event)">
                <div class="form-group">
                    <label>Имя:</label>
                    <input type="text" name="name" value="${master.name || ''}" required>
                </div>
                <div class="form-group">
                    <label>Специализация:</label>
                    <input type="text" name="specialization" value="${master.specialization || ''}">
                </div>
                <div class="form-group">
                    <label>Стаж (лет):</label>
                    <input type="number" name="experience" value="${master.experience || 0}" min="0">
                </div>
                <div class="form-group">
                    <label>Рейтинг:</label>
                    <input type="number" name="rating" step="0.1" min="0" max="5" value="${master.rating || 0}">
                </div>
                <div class="form-group">
                    <label>ID пользователя:</label>
                    <input type="number" name="userId" value="${master.userId || ''}" required>
                </div>
                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                    <button type="submit" class="btn btn-primary">Сохранить изменения</button>
                </div>
            </form>
        `;
        showModal(form);
    } catch (error) {
        alert('Ошибка загрузки мастера: ' + error.message);
    }
}

async function saveMaster(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const master = {
        name: formData.get('name'),
        specialization: formData.get('specialization'),
        experience: parseInt(formData.get('experience')) || 0,
        rating: parseFloat(formData.get('rating')) || 0,
        userId: parseInt(formData.get('userId'))
    };
    
    try {
        const url = editingMasterId 
            ? `${API_BASE}/masters/${editingMasterId}`
            : `${API_BASE}/masters`;
        const method = editingMasterId ? 'PUT' : 'POST';
        
        const response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(master)
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Ошибка сохранения');
        }
        
        closeModal();
        editingMasterId = null;
        loadMasters();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function deleteMaster(id) {
    if (!confirm('Удалить мастера?')) return;
    try {
        await fetch(`${API_BASE}/masters/${id}`, { method: 'DELETE' });
        loadMasters();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

// ========== ЗАПИСИ ==========
async function loadAppointments() {
    try {
        const response = await fetch(`${API_BASE}/appointments`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        document.getElementById('appointments-list').innerHTML = 
            '<div class="error">Ошибка загрузки записей: ' + error.message + '</div>';
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('appointments-list');
    if (appointments.length === 0) {
        container.innerHTML = '<div class="loading">Нет записей</div>';
        return;
    }
    
    container.innerHTML = appointments.map(apt => `
        <div class="card">
            <div class="card-header">
                <div class="card-title">Запись #${apt.id}</div>
                <div class="card-actions">
                    <span class="status-badge status-${apt.status.toLowerCase()}">${apt.status}</span>
                </div>
            </div>
            <div class="card-body">
                <div class="card-field">
                    <span class="card-label">Дата:</span>
                    <span class="card-value">${apt.date}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Время:</span>
                    <span class="card-value">${apt.time}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Клиент ID:</span>
                    <span class="card-value">${apt.clientId}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Мастер ID:</span>
                    <span class="card-value">${apt.masterId}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Услуга ID:</span>
                    <span class="card-value">${apt.serviceId}</span>
                </div>
                ${apt.status === 'Запланирована' ? `
                    <div class="form-actions" style="margin-top: 10px;">
                        <button class="btn btn-small btn-success" onclick="completeAppointment(${apt.id})">✓ Выполнена</button>
                        <button class="btn btn-small btn-danger" onclick="cancelAppointment(${apt.id})">✗ Отменить</button>
                    </div>
                ` : ''}
            </div>
        </div>
    `).join('');
}

async function showAddAppointmentForm() {
    try {
        // Загружаем списки для выбора
        const [servicesRes, mastersRes, usersRes] = await Promise.all([
            fetch(`${API_BASE}/services`),
            fetch(`${API_BASE}/masters`),
            fetch(`${API_BASE}/users`)
        ]);
        
        const services = await servicesRes.json();
        const masters = await mastersRes.json();
        const users = await usersRes.json();
        
        const today = new Date().toISOString().split('T')[0];
        const defaultTime = '10:00';
        
        const servicesOptions = services.map(s => 
            `<option value="${s.id}">${s.name} (${s.price} ₽)</option>`
        ).join('');
        
        const mastersOptions = masters.map(m => 
            `<option value="${m.id}">${m.name}</option>`
        ).join('');
        
        const clientsOptions = users.filter(u => u.role === 'Клиент').map(u => 
            `<option value="${u.id}">${u.username} (${u.email})</option>`
        ).join('');
        
        const form = `
            <h2>Новая запись</h2>
            <form onsubmit="saveAppointment(event)">
                <div class="form-group">
                    <label>Клиент:</label>
                    <select name="clientId" required>
                        <option value="">Выберите клиента</option>
                        ${clientsOptions}
                    </select>
                </div>
                <div class="form-group">
                    <label>Мастер:</label>
                    <select name="masterId" required>
                        <option value="">Выберите мастера</option>
                        ${mastersOptions}
                    </select>
                </div>
                <div class="form-group">
                    <label>Услуга:</label>
                    <select name="serviceId" required>
                        <option value="">Выберите услугу</option>
                        ${servicesOptions}
                    </select>
                </div>
                <div class="form-group">
                    <label>Дата:</label>
                    <input type="date" name="date" value="${today}" required>
                </div>
                <div class="form-group">
                    <label>Время:</label>
                    <input type="time" name="time" value="${defaultTime}" required>
                </div>
                <div class="form-actions">
                    <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                    <button type="submit" class="btn btn-primary">Создать запись</button>
                </div>
            </form>
        `;
        showModal(form);
    } catch (error) {
        alert('Ошибка загрузки данных: ' + error.message);
    }
}

async function saveAppointment(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const appointment = {
        clientId: parseInt(formData.get('clientId')),
        masterId: parseInt(formData.get('masterId')),
        serviceId: parseInt(formData.get('serviceId')),
        date: formData.get('date'),
        time: formData.get('time') + ':00',
        status: 'Запланирована'
    };
    
    try {
        const response = await fetch(`${API_BASE}/appointments`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(appointment)
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Ошибка создания записи');
        }
        
        closeModal();
        loadAppointments();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function completeAppointment(id) {
    try {
        await fetch(`${API_BASE}/appointments/${id}/complete`, { method: 'PUT' });
        loadAppointments();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function cancelAppointment(id) {
    if (!confirm('Отменить запись?')) return;
    try {
        await fetch(`${API_BASE}/appointments/${id}/cancel`, { method: 'PUT' });
        loadAppointments();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

// ========== ПОЛЬЗОВАТЕЛИ ==========
async function loadUsers() {
    try {
        const response = await fetch(`${API_BASE}/users`);
        const users = await response.json();
        displayUsers(users);
    } catch (error) {
        document.getElementById('users-list').innerHTML = 
            '<div class="error">Ошибка загрузки пользователей: ' + error.message + '</div>';
    }
}

function displayUsers(users) {
    const container = document.getElementById('users-list');
    if (users.length === 0) {
        container.innerHTML = '<div class="loading">Нет пользователей</div>';
        return;
    }
    
    container.innerHTML = users.map(user => `
        <div class="card">
            <div class="card-header">
                <div class="card-title">${user.username}</div>
                <div class="card-actions">
                    <span class="role-badge">${user.role}</span>
                </div>
            </div>
            <div class="card-body">
                <div class="card-field">
                    <span class="card-label">Email:</span>
                    <span class="card-value">${user.email}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Телефон:</span>
                    <span class="card-value">${user.phone || 'Не указан'}</span>
                </div>
            </div>
        </div>
    `).join('');
}

function showAddUserForm() {
    const form = `
        <h2>Добавить пользователя</h2>
        <form onsubmit="saveUser(event)">
            <div class="form-group">
                <label>Имя пользователя:</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Пароль:</label>
                <input type="password" name="password" required>
            </div>
            <div class="form-group">
                <label>Роль:</label>
                <select name="role" required>
                    <option value="">Выберите роль</option>
                    <option value="Администратор">Администратор</option>
                    <option value="Мастер">Мастер</option>
                    <option value="Клиент">Клиент</option>
                </select>
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required>
            </div>
            <div class="form-group">
                <label>Телефон:</label>
                <input type="text" name="phone" placeholder="+7 (999) 123-45-67">
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                <button type="submit" class="btn btn-primary">Создать пользователя</button>
            </div>
        </form>
    `;
    showModal(form);
}

async function saveUser(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const user = {
        username: formData.get('username'),
        password: formData.get('password'),
        role: formData.get('role'),
        email: formData.get('email'),
        phone: formData.get('phone') || null
    };
    
    try {
        const response = await fetch(`${API_BASE}/users`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(user)
        });
        
        if (!response.ok) {
            const errorData = await response.text();
            throw new Error(errorData || 'Ошибка создания пользователя');
        }
        
        closeModal();
        loadUsers();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

// ========== ОТЧЁТЫ ==========
async function loadReports() {
    try {
        const response = await fetch(`${API_BASE}/reports`);
        const reports = await response.json();
        displayReports(reports);
    } catch (error) {
        document.getElementById('reports-list').innerHTML = 
            '<div class="error">Ошибка загрузки отчётов: ' + error.message + '</div>';
    }
}

function displayReports(reports) {
    const container = document.getElementById('reports-list');
    if (reports.length === 0) {
        container.innerHTML = '<div class="loading">Нет отчётов</div>';
        return;
    }
    
    container.innerHTML = reports.map(report => `
        <div class="card">
            <div class="card-header">
                <div class="card-title">Отчёт #${report.id}</div>
            </div>
            <div class="card-body">
                <div class="card-field">
                    <span class="card-label">Дата:</span>
                    <span class="card-value">${report.reportDate}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Клиентов:</span>
                    <span class="card-value">${report.totalClients}</span>
                </div>
                <div class="card-field">
                    <span class="card-label">Выручка:</span>
                    <span class="card-value">${report.totalIncome} ₽</span>
                </div>
            </div>
        </div>
    `).join('');
}

function showGenerateReportForm() {
    const today = new Date().toISOString().split('T')[0];
    const form = `
        <h2>Сгенерировать отчёт</h2>
        <form onsubmit="generateReport(event)">
            <div class="form-group">
                <label>Дата:</label>
                <input type="date" name="date" value="${today}" required>
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="closeModal()">Отмена</button>
                <button type="submit" class="btn btn-primary">Сгенерировать</button>
            </div>
        </form>
    `;
    showModal(form);
}

async function generateReport(event) {
    event.preventDefault();
    const date = event.target.date.value;
    try {
        await fetch(`${API_BASE}/reports/generate/${date}`, { method: 'POST' });
        closeModal();
        loadReports();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

// ========== ЭКСПОРТ ==========
async function exportData(type, format) {
    try {
        const response = await fetch(`${API_BASE}/export-import/export/${type}/${format}`);
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${type}.${format}`;
        a.click();
        window.URL.revokeObjectURL(url);
    } catch (error) {
        alert('Ошибка экспорта: ' + error.message);
    }
}

// ========== МОДАЛЬНОЕ ОКНО ==========
function showModal(content) {
    document.getElementById('modal-body').innerHTML = content;
    document.getElementById('modal').style.display = 'block';
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        closeModal();
    }
}

