# 🌤️ Weather App

A modern, full-stack weather application that provides real-time weather data with support for metric and imperial units, multiple languages, and an elegant user interface.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4-brightgreen)
![Redis](https://img.shields.io/badge/Redis-Cache-red)
![JavaScript](https://img.shields.io/badge/JavaScript-ES6+-yellow)
![TailwindCSS](https://img.shields.io/badge/TailwindCSS-3.x-blue)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Core Functionality
- 🌍 **Real-time Weather Data**: Current conditions and 7-day forecast
- 📍 **Geolocation Support**: Get weather for your current location
- 🌡️ **Dual Unit System**: Switch between metric (°C, km/h) and imperial (°F, mph)
- 🌐 **Multilingual**: Support for English, Spanish, French, German, and Portuguese
- 🔔 **Toast Notifications**: Elegant error and success messages
- ⚡ **Redis Caching**: Fast response times with 60-minute cache

### User Experience
- 🎨 **Modern UI**: Clean, responsive design with Tailwind CSS
- 🌓 **Day/Night Icons**: Dynamic weather condition icons
- 📊 **Detailed Metrics**: UV index, humidity, wind speed, visibility, and more
- 📱 **Mobile Responsive**: Works seamlessly on all devices

### Backend Features
- � **Stateless REST API**: No database, pure API service
- ⚡ **Redis Caching**: Fast response times with 60-minute cache
- 🛡️ **Robust Error Handling**: Custom exceptions with meaningful messages
- ✅ **Input Validation**: Strict parameter validation
- 📝 **Comprehensive Logging**: SLF4J for production-ready logging

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.4
- **Language**: Java 21
- **Cache**: Redis
- **Validation**: Jakarta Validation
- **Build Tool**: Maven
- **External API**: WeatherAPI.com

### Frontend
- **Core**: HTML5, CSS3, JavaScript ES6+
- **Styling**: Tailwind CSS
- **HTTP Client**: Fetch API
- **Icons**: Weather icons from WeatherAPI

## 🏗️ Architecture

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │ HTTP
       ▼
┌─────────────────┐
│  Frontend (JS)  │
│  - app.js       │
│  - index.html   │
└──────┬──────────┘
       │ REST API
       ▼
┌─────────────────────────┐
│   Spring Boot Backend   │
│  ┌─────────────────┐    │
│  │  Controller     │    │
│  └────────┬────────┘    │
│           ▼             │
│  ┌─────────────────┐    │
│  │  Service Layer  │    │
│  └────────┬────────┘    │
│           ▼             │
│  ┌─────────────────┐    │
│  │  Redis Cache    │    │
│  └────────┬────────┘    │
└───────────┼─────────────┘
            ▼
    ┌───────────────┐
    │  WeatherAPI   │
    └───────────────┘
```

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **Redis** (running on localhost:6379)
- **WeatherAPI Key** ([Get one free here](https://www.weatherapi.com/))

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/weather-app.git
cd weather-app
```

### 2. Backend Setup

#### Configure Application Properties

Create or edit `backend/src/main/resources/application.properties`:

```properties
# Server Configuration
server.port=8080

# Weather API Configuration
weather.api.url=https://api.weatherapi.com/v1/forecast.json
weather.api.key=YOUR_API_KEY_HERE

# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.data.redis.timeout=60000

# Logging
logging.level.com.weatherapp=INFO
```

#### Start Redis

```bash
# macOS (with Homebrew)
brew services start redis

# Linux
sudo systemctl start redis

# Docker
docker run -d -p 6379:6379 redis:latest
```

#### Build and Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## ⚙️ Configuration

### Environment Variables (Optional)

For production, use environment variables instead of hardcoding in `application.properties`:

```bash
export WEATHER_API_KEY=your_api_key
export SPRING_REDIS_HOST=your_redis_host
export SPRING_REDIS_PORT=6379
```

Update `application.properties`:

```properties
weather.api.key=${WEATHER_API_KEY}
spring.data.redis.host=${SPRING_REDIS_HOST:localhost}
spring.data.redis.port=${SPRING_REDIS_PORT:6379}
```

### CORS Configuration

⚠️ **Before deploying to production**, update `CorsConfig.java`:

```java
.allowedOrigins(
    "https://your-production-domain.com",
    "https://www.your-production-domain.com"
)
```

Currently set to `allowedOriginPatterns("*")` for development only.

##  API Documentation

### Get Weather Data

**Endpoint:** `GET /api/v1/weather/{city}`

**Parameters:**

| Parameter | Type          | Default  | Description                        |
| --------- | ------------- | -------- | ---------------------------------- |
| `city`    | String (path) | Required | City name or coordinates           |
| `day`     | Integer       | 1        | Number of forecast days (1-7)      |
| `lang`    | String        | en       | Language code (en, es, fr, de, pt) |
| `metric`  | String        | metric   | Unit system (metric, imperial)     |

**Example Request:**

```bash
curl "http://localhost:8080/api/v1/weather/London?day=7&lang=en&metric=metric"
```

**Example Response:**

```json
{
  "success": true,
  "data": {
    "location": {
      "name": "London",
      "region": "City of London, Greater London",
      "country": "United Kingdom",
      "localTime": "2024-12-13 19:00"
    },
    "current": {
      "temperature": 8.0,
      "feelsLike": 5.2,
      "humidity": 81,
      "windSpeed": 20.5,
      "uvIndex": 1.0,
      "visibility": 10.0,
      "isDay": 0,
      "condition": {
        "text": "Partly cloudy",
        "icon": "//cdn.weatherapi.com/weather/64x64/night/116.png"
      }
    },
    "forecast": {
      "forecastday": [...]
    }
  },
  "message": "Weather data",
  "timestamp": "2024-12-13T19:00:00"
}
```

**Error Responses:**

- `400 Bad Request`: Invalid parameters
- `404 Not Found`: City not found
- `500 Internal Server Error`: Server error
- `502 Bad Gateway`: External API error

## 📁 Project Structure

```
weather-app/
├── backend/                      # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/.../weather_backend/
│   │   │   │   ├── config/       # Configuration classes
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   ├── RedisConfig.java
│   │   │   │   │   └── WeatherProperties.java
│   │   │   │   ├── controllers/  # REST Controllers
│   │   │   │   │   └── WeatherApiController.java
│   │   │   │   ├── dto/          # Data Transfer Objects
│   │   │   │   │   ├── ApiResponse.java
│   │   │   │   │   ├── ErrorDetail.java
│   │   │   │   │   ├── WeatherApiError.java
│   │   │   │   │   ├── WeatherDTO.java
│   │   │   │   │   └── WeatherResponseDTO.java
│   │   │   │   ├── exceptions/   # Custom Exceptions
│   │   │   │   │   ├── CityNotFound.java
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── WeatherApiException.java
│   │   │   │   │   └── WeatherApiParseException.java
│   │   │   │   ├── interfaces/   # Service Interfaces
│   │   │   │   │   └── WeatherProvider.java
│   │   │   │   ├── services/     # Business Logic
│   │   │   │   │   └── WeatherApiServices.java
│   │   │   │   └── WeatherBackendApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                 # Unit Tests
│   └── pom.xml                   # Maven Dependencies
│
└── frontend/                     # Frontend Application
    ├── index.html                # Main HTML
    ├── app.js                    # JavaScript Logic
    └── assets/                   # Images, Icons, etc.
```

## 🔒 Security Features

- ✅ **API Key Protection**: Keys sanitized in logs (displayed as `***MASKED***`)
- ✅ **Input Validation**: Strict validation on all endpoints
- ✅ **Error Handling**: No sensitive data exposed in error messages
- ⚠️ **CORS**: Update before production deployment (currently allows all origins for development)

## 🧪 Testing

### Run Backend Tests

```bash
cd backend
mvn test
```

### Manual Testing

1. Start the backend
2. Open frontend in browser
3. Test various cities and scenarios
4. Check browser console for any errors
5. Verify Redis cache in Redis CLI:

```bash
redis-cli
> KEYS *
> GET "weather_cache::London_7_en_metric"
```

## 🐛 Troubleshooting

### Common Issues

**Backend won't start:**
- Check if Redis is running: `redis-cli ping` (should return `PONG`)
- Verify Java version: `java -version` (must be 17+)
- Check if port 8080 is available

**API returns 404:**
- Verify WeatherAPI key is correct
- Check internet connection
- Review logs: `tail -f backend/logs/application.log`

**Frontend CORS errors:**
- Ensure backend is running on port 8080
- Check browser console for specific error
- Verify CORS configuration in `CorsConfig.java`

**Cache not working:**
- Check Redis connection
- Verify cache configuration in `RedisConfig.java`
- Check TTL: cache expires after 60 minutes

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Code Style

- **Backend**: Follow standard Java conventions
- **Frontend**: Use ES6+ features, maintain consistent formatting
- **Commits**: Use conventional commit messages

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Your Name** - [GitHub Profile](https://github.com/yourusername)

## 🙏 Acknowledgments

- [WeatherAPI.com](https://www.weatherapi.com/) for providing weather data
- [Tailwind CSS](https://tailwindcss.com/) for the styling framework
- [Spring Boot](https://spring.io/projects/spring-boot) for the backend framework
- [Redis](https://redis.io/) for caching solution

---

⭐ If you found this project helpful, please give it a star!

📧 Questions? Open an issue or contact [your.email@example.com](mailto:your.email@example.com)