const API_URL = 'http://localhost:8080/api/v1/weather';
const currentDays = 7;
let currentLang = 'es';
let currentSystem = 'metric';
let lastCity = '';


function showToast(message, type = 'error') {
    const container = document.getElementById('toastContainer');
    if (!container) return;


    const toast = document.createElement('div');
    toast.className = `toast-enter px-6 py-4 rounded-xl shadow-2xl flex items-center gap-3 min-w-[300px] max-w-md`;


    if (type === 'error') {
        toast.classList.add('bg-red-600', 'text-white');
        toast.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="15" y1="9" x2="9" y2="15"></line>
                <line x1="9" y1="9" x2="15" y2="15"></line>
            </svg>
            <span class="flex-1 font-medium">${message}</span>
        `;
    } else if (type === 'success') {
        toast.classList.add('bg-green-600', 'text-white');
        toast.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
            </svg>
            <span class="flex-1 font-medium">${message}</span>
        `;
    } else if (type === 'warning') {
        toast.classList.add('bg-yellow-600', 'text-white');
        toast.innerHTML = `
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
                <line x1="12" y1="9" x2="12" y2="13"></line>
                <line x1="12" y1="17" x2="12.01" y2="17"></line>
            </svg>
            <span class="flex-1 font-medium">${message}</span>
        `;
    }

    container.appendChild(toast);


    setTimeout(() => {
        toast.classList.remove('toast-enter');
        toast.classList.add('toast-exit');


        setTimeout(() => {
            container.removeChild(toast);
        }, 300);
    }, 4000);
}


const translations = {
    es: {
        searchPlaceholder: 'Buscar ciudad...',
        loadingText: 'Buscando clima...',
        todayHighlights: 'Aspectos destacados de hoy',
        uvIndex: 'Índice UV',
        windStatus: 'Estado del viento',
        humidity: 'Humedad',
        feelsLike: 'Sensación térmica',
        visibility: 'Visibilidad',
        today: 'Hoy',
        city: 'Ciudad',
        days: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
        daysShort: ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb']
    },
    en: {
        searchPlaceholder: 'Search city...',
        loadingText: 'Searching weather...',
        todayHighlights: "Today's Highlights",
        uvIndex: 'UV Index',
        windStatus: 'Wind Status',
        humidity: 'Humidity',
        feelsLike: 'Feels Like',
        visibility: 'Visibility',
        today: 'Today',
        city: 'City',
        days: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        daysShort: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
    },
    fr: {
        searchPlaceholder: 'Rechercher une ville...',
        loadingText: 'Recherche météo...',
        todayHighlights: "Points forts d'aujourd'hui",
        uvIndex: 'Indice UV',
        windStatus: 'État du vent',
        humidity: 'Humidité',
        feelsLike: 'Ressenti',
        visibility: 'Visibilité',
        today: "Aujourd'hui",
        city: 'Ville',
        days: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
        daysShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam']
    },
    de: {
        searchPlaceholder: 'Stadt suchen...',
        loadingText: 'Wetter suchen...',
        todayHighlights: 'Heutige Highlights',
        uvIndex: 'UV-Index',
        windStatus: 'Windstatus',
        humidity: 'Luftfeuchtigkeit',
        feelsLike: 'Gefühlt',
        visibility: 'Sichtweite',
        today: 'Heute',
        city: 'Stadt',
        days: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
        daysShort: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa']
    },
    pt: {
        searchPlaceholder: 'Procurar cidade...',
        loadingText: 'Procurando clima...',
        todayHighlights: 'Destaques de hoje',
        uvIndex: 'Índice UV',
        windStatus: 'Estado do vento',
        humidity: 'Umidade',
        feelsLike: 'Sensação',
        visibility: 'Visibilidade',
        today: 'Hoje',
        city: 'Cidade',
        days: ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'],
        daysShort: ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb']
    }
};


function updateUILanguage() {
    const t = translations[currentLang];


    document.getElementById('cityInput').placeholder = t.searchPlaceholder;


    document.querySelector('#loadingSpinner p').textContent = t.loadingText;


    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (t[key]) {
            el.textContent = t[key];
        }
    });
}


document.getElementById('cityInput').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        buscarClima();
    }
});

document.getElementById('getLocation').addEventListener('click', function () {
    obtenerUbicacion();
});


updateUILanguage();


const celsiusButton = document.querySelector('.flex.bg-gray-700 button:nth-child(1)');
const fahrenheitButton = document.querySelector('.flex.bg-gray-700 button:nth-child(2)');

function updateSystemButtons() {
    if (currentSystem === 'metric') {
        celsiusButton.classList.add('bg-white', 'text-black');
        celsiusButton.classList.remove('text-white', 'hover:bg-gray-600');
        fahrenheitButton.classList.remove('bg-white', 'text-black');
        fahrenheitButton.classList.add('text-white', 'hover:bg-gray-600');
    } else {
        fahrenheitButton.classList.add('bg-white', 'text-black');
        fahrenheitButton.classList.remove('text-white', 'hover:bg-gray-600');
        celsiusButton.classList.remove('bg-white', 'text-black');
        celsiusButton.classList.add('text-white', 'hover:bg-gray-600');
    }
}

function updateUnits() {
    const windUnit = document.getElementById('windUnit');
    const feelsLikeUnit = document.getElementById('feelsLikeUnit');
    const visibilityUnit = document.getElementById('visibilityUnit');

    if (currentSystem === 'metric') {
        if (windUnit) windUnit.textContent = 'km/h';
        if (feelsLikeUnit) feelsLikeUnit.textContent = '°C';
        if (visibilityUnit) visibilityUnit.textContent = 'km';
    } else {
        if (windUnit) windUnit.textContent = 'mph';
        if (feelsLikeUnit) feelsLikeUnit.textContent = '°F';
        if (visibilityUnit) visibilityUnit.textContent = 'mi';
    }
}

celsiusButton.addEventListener('click', () => {
    if (currentSystem !== 'metric') {
        currentSystem = 'metric';
        updateSystemButtons();
        updateUnits();
        if (lastCity) {
            buscarClima(lastCity);
        }
    }
});

fahrenheitButton.addEventListener('click', () => {
    if (currentSystem !== 'imperial') {
        currentSystem = 'imperial';
        updateSystemButtons();
        updateUnits();
        if (lastCity) {
            buscarClima(lastCity);
        }
    }
});


updateSystemButtons();
updateUnits();


const langButton = document.getElementById('langButton');
const langDropdown = document.getElementById('langDropdown');
const langArrow = document.getElementById('langArrow');
const currentLangEl = document.getElementById('currentLang');


langButton.addEventListener('click', (e) => {
    e.stopPropagation();
    langDropdown.classList.toggle('hidden');
    langArrow.style.transform = langDropdown.classList.contains('hidden') ? 'rotate(0deg)' : 'rotate(180deg)';
});


document.addEventListener('click', () => {
    langDropdown.classList.add('hidden');
    langArrow.style.transform = 'rotate(0deg)';
});


document.querySelectorAll('.lang-option').forEach(option => {
    option.addEventListener('click', (e) => {
        e.stopPropagation();
        const selectedLang = option.dataset.lang;
        currentLang = selectedLang;
        currentLangEl.textContent = selectedLang.toUpperCase();


        updateUILanguage();


        langDropdown.classList.add('hidden');
        langArrow.style.transform = 'rotate(0deg)';


        if (lastCity) {
            buscarClima(lastCity);
        }
    });
});


function obtenerUbicacion() {
    if (!navigator.geolocation) {
        showToast('Tu navegador no soporta geolocalización', 'warning');
        return;
    }

    showLoading();

    navigator.geolocation.getCurrentPosition(
        async (position) => {
            const { latitude, longitude } = position.coords;
            console.log(`📍 Ubicación obtenida: ${latitude}, ${longitude}`);

            try {

                const coordString = `${latitude},${longitude}`;
                await buscarClima(coordString);


                document.getElementById('cityInput').value = coordString;
            } catch (error) {
                console.error("💥 Error al buscar clima por ubicación:", error);
                hideLoading();
            }
        },
        (error) => {
            hideLoading();
            let mensaje = '';
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    mensaje = 'Permiso de ubicación denegado. Por favor, permite el acceso a tu ubicación.';
                    break;
                case error.POSITION_UNAVAILABLE:
                    mensaje = 'Información de ubicación no disponible.';
                    break;
                case error.TIMEOUT:
                    mensaje = 'Tiempo de espera agotado al obtener ubicación.';
                    break;
                default:
                    mensaje = 'Error desconocido al obtener ubicación.';
            }
            showToast(mensaje, 'warning');
            console.error("Error de geolocalización:", error);
        },
        {
            enableHighAccuracy: true,
            timeout: 10000,
            maximumAge: 0
        }
    );
}

async function buscarClima(cityOverride = null) {
    const cityInput = document.getElementById('cityInput');
    const city = cityOverride || cityInput.value.trim();

    if (!city) return;

    lastCity = city;


    showLoading();

    try {

        const url = `${API_URL}/${encodeURIComponent(city)}?day=${currentDays}&lang=${currentLang}&metric=${currentSystem}`;
        console.log("Requesting:", url);

        const response = await fetch(url);
        const apiResponse = await response.json();


        if (apiResponse.success) {
            const weather = apiResponse.data;
            const current = weather.current;
            const location = weather.location;


            const tempSymbol = currentSystem === 'metric' ? '°C' : '°F';
            document.getElementById('temp').textContent = Math.round(current.temperature) + tempSymbol;


            document.getElementById('condition').textContent = current.condition.text;


            const cityDisplay = `${location.name}, ${location.country}`;
            document.getElementById('cityNameDisplay').textContent = cityDisplay;


            const date = new Date(location.localTime);
            const t = translations[currentLang];
            document.getElementById('day').textContent = t.days[date.getDay()];
            document.getElementById('hour').textContent = date.toLocaleTimeString('es-ES', {
                hour: '2-digit',
                minute: '2-digit'
            });


            const mainWeatherIcon = document.getElementById('mainWeatherIcon');
            if (mainWeatherIcon && current.condition.icon) {
                const largeIconUrl = current.condition.icon.replace('64x64', '128x128');
                mainWeatherIcon.src = 'https:' + largeIconUrl;
                mainWeatherIcon.alt = current.condition.text;
            }


            updateSmallIcon(current.condition);


            document.getElementById('uvIndex').textContent = current.uvIndex;
            document.getElementById('windSpeed').textContent = Math.round(current.windSpeed);
            document.getElementById('humidity').textContent = current.humidity;
            document.getElementById('humidityBar').style.width = current.humidity + "%";
            document.getElementById('feelsLike').textContent = Math.round(current.feelsLike);
            document.getElementById('visibility').textContent = current.visibility;


            updateUnits();


            if (weather.forecast && weather.forecast.forecastday) {
                renderForecast(weather.forecast.forecastday);
            }

            console.log("✅ Datos cargados correctamente para: " + city);

        } else {

            showToast(apiResponse.message, 'error');
        }

    } catch (error) {
        console.error("💥 Error de red:", error);
        showToast(`No se pudo obtener el clima para "${city}". Verifica que el backend esté corriendo.`, 'error');
    } finally {
        hideLoading();
    }
}

function updateSmallIcon(condition) {
    const weatherIconSvg = document.getElementById('weatherIconSvg');
    if (condition.icon) {
        const iconImg = document.createElement('img');
        iconImg.src = 'https:' + condition.icon;
        iconImg.alt = condition.text;
        iconImg.className = 'w-6 h-6';
        iconImg.id = 'weatherIconSvg';

        if (weatherIconSvg) {
            weatherIconSvg.replaceWith(iconImg);
        }
    }
}


function getDayName(dateString) {

    const date = new Date(dateString + 'T00:00:00');
    const t = translations[currentLang];
    return t.daysShort[date.getDay()];
}


function renderForecast(forecastDays) {
    const container = document.getElementById('forecastContainer');
    if (!container) return;

    container.innerHTML = '';
    const t = translations[currentLang];

    forecastDays.forEach((dayData, index) => {

        const isToday = index === 0;

        const card = document.createElement('div');
        card.className = `bg-gray-700/50 p-4 rounded-2xl flex flex-col items-center gap-2 hover:bg-gray-600/50 transition ${isToday ? 'ring-2 ring-blue-500' : ''}`;

        const dayName = document.createElement('span');
        dayName.className = isToday ? 'text-sm text-blue-400 font-bold' : 'text-sm text-gray-400';
        dayName.textContent = isToday ? t.today : getDayName(dayData.date);

        const icon = document.createElement('img');
        icon.src = 'https:' + dayData.day.condition.icon;
        icon.alt = dayData.day.condition.text;
        icon.className = 'w-10 h-10';

        const tempContainer = document.createElement('div');
        tempContainer.className = 'flex flex-col items-center';

        const maxTemp = document.createElement('span');
        maxTemp.className = 'font-bold text-white';
        maxTemp.textContent = Math.round(dayData.day.maxTemp) + '°';

        const minTemp = document.createElement('span');
        minTemp.className = 'text-xs text-gray-400';
        minTemp.textContent = Math.round(dayData.day.minTemp) + '°';

        tempContainer.appendChild(maxTemp);
        tempContainer.appendChild(minTemp);

        card.appendChild(dayName);
        card.appendChild(icon);
        card.appendChild(tempContainer);

        container.appendChild(card);
    });
}

function showLoading() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.classList.remove('hidden');
        spinner.classList.add('flex');
    }
}

function hideLoading() {
    const spinner = document.getElementById('loadingSpinner');
    if (spinner) {
        spinner.classList.remove('flex');
        spinner.classList.add('hidden');
    }
}