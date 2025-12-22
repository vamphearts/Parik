const API_BASE = 'http://localhost:8080/api';

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    loadStatistics();
});

// Переключение вкладок
function switchTab(tabName) {
    const url = new URL(window.location);
    url.searchParams.set('tab', tabName);
    window.location.href = url.toString();
}

// Поиск услуг
function searchServices() {
    const query = document.getElementById('search-services').value;
    const url = new URL(window.location);
    if (query) {
        url.searchParams.set('search', query);
        url.searchParams.set('tab', 'services');
    } else {
        url.searchParams.delete('search');
    }
    window.location.href = url.toString();
}

// Сортировка услуг
function sortServices() {
    const select = document.getElementById('sort-services');
    const [sortBy, order] = select.value.split(',');
    const url = new URL(window.location);
    url.searchParams.set('sortBy', sortBy);
    url.searchParams.set('order', order);
    url.searchParams.set('tab', 'services');
    window.location.href = url.toString();
}

// Поиск записей
function searchAppointments() {
    const query = document.getElementById('search-appointments').value;
    const url = new URL(window.location);
    if (query) {
        url.searchParams.set('search', query);
        url.searchParams.set('tab', 'appointments');
    } else {
        url.searchParams.delete('search');
    }
    window.location.href = url.toString();
}

// Загрузка статистики
async function loadStatistics() {
    try {
        const response = await fetch(`${API_BASE}/statistics`);
        const stats = await response.json();
        displayStatistics(stats);
    } catch (error) {
        console.error('Ошибка загрузки статистики:', error);
    }
}

function displayStatistics(stats) {
    // Можно добавить визуализацию статистики
    console.log('Статистика:', stats);
}

// Модальные окна для форм
let editingServiceId = null;
let editingMasterId = null;

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
                    <input type="text" name="name" value="${escapeHtml(service.name || '')}" required>
                </div>
                <div class="form-group">
                    <label>Описание:</label>
                    <textarea name="description" rows="3">${escapeHtml(service.description || '')}</textarea>
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
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function deleteService(id) {
    if (!confirm('Удалить услугу?')) return;
    try {
        await fetch(`${API_BASE}/services/${id}`, { method: 'DELETE' });
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
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
                    <input type="text" name="name" value="${escapeHtml(master.name || '')}" required>
                </div>
                <div class="form-group">
                    <label>Специализация:</label>
                    <input type="text" name="specialization" value="${escapeHtml(master.specialization || '')}">
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
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function deleteMaster(id) {
    if (!confirm('Удалить мастера?')) return;
    try {
        await fetch(`${API_BASE}/masters/${id}`, { method: 'DELETE' });
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function showAddAppointmentForm() {
    try {
        const [servicesRes, mastersRes, usersRes] = await Promise.all([
            fetch(`${API_BASE}/services`, { credentials: 'include' }),
            fetch(`${API_BASE}/masters`, { credentials: 'include' }),
            fetch(`${API_BASE}/users`, { credentials: 'include' })
        ]);
        
        // Проверяем каждый ответ отдельно для более детальной ошибки
        if (!servicesRes.ok) {
            const errorText = await servicesRes.text();
            throw new Error(`Ошибка загрузки услуг: ${servicesRes.status} ${errorText}`);
        }
        if (!mastersRes.ok) {
            const errorText = await mastersRes.text();
            throw new Error(`Ошибка загрузки мастеров: ${mastersRes.status} ${errorText}`);
        }
        if (!usersRes.ok) {
            const errorText = await usersRes.text();
            throw new Error(`Ошибка загрузки пользователей: ${usersRes.status} ${errorText}`);
        }
        
        const services = await servicesRes.json();
        const mastersData = await mastersRes.json();
        const users = await usersRes.json();
        
        // Проверяем, что masters - это массив
        const masters = Array.isArray(mastersData) ? mastersData : [];
        // Проверяем, что services - это массив
        const servicesArray = Array.isArray(services) ? services : [];
        // Проверяем, что users - это массив
        const usersArray = Array.isArray(users) ? users : [];
        
        const today = new Date().toISOString().split('T')[0];
        const defaultTime = '10:00';
        
        const servicesOptions = servicesArray.map(s => 
            `<option value="${s.id}">${escapeHtml(s.name)} (${s.price} ₽)</option>`
        ).join('');
        
        const mastersOptions = masters.map(m => 
            `<option value="${m.id}">${escapeHtml(m.name)}</option>`
        ).join('');
        
        const clientsOptions = usersArray.filter(u => u.role === 'Клиент').map(u => 
            `<option value="${u.id}">${escapeHtml(u.username)} (${escapeHtml(u.email)})</option>`
        ).join('');
        
        // Если пользователь - клиент, автоматически подставляем его ID
        const isClient = window.currentUser && window.currentUser.role === 'Клиент' && window.currentUser.id;
        const clientIdField = isClient
            ? `<input type="hidden" name="clientId" value="${window.currentUser.id}">`
            : `<div class="form-group">
                    <label>Клиент:</label>
                    <select name="clientId" required>
                        <option value="">Выберите клиента</option>
                        ${clientsOptions}
                    </select>
                </div>`;
        
        const form = `
            <h2>Новая запись</h2>
            <form onsubmit="saveAppointment(event)">
                ${clientIdField}
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
        console.error('Ошибка загрузки данных:', error);
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
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function completeAppointment(id) {
    try {
        await fetch(`${API_BASE}/appointments/${id}/complete`, { method: 'PUT' });
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

async function cancelAppointment(id) {
    if (!confirm('Отменить запись?')) return;
    try {
        await fetch(`${API_BASE}/appointments/${id}/cancel`, { method: 'PUT' });
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
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
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
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
        location.reload();
    } catch (error) {
        alert('Ошибка: ' + error.message);
    }
}

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

// Защита от XSS - экранирование HTML
function escapeHtml(text) {
    if (!text) return '';
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.toString().replace(/[&<>"']/g, m => map[m]);
}

