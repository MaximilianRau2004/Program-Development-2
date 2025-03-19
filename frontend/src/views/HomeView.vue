<template>
  <div class="start-page">
    <div class="welcome-container">
      <div class="logo-container">
        <div class="logo">✓</div>
      </div>
      <h1>Willkommen bei der Todo-App!</h1>
      <p class="tagline">Organisiere Todos effizient und weise sie den richtigen Assignees zu.</p>
      <div class="features-container">
        <div class="feature">
          <div class="feature-icon">📋</div>
          <div class="feature-text">Aufgaben organisieren</div>
        </div>
        <div class="feature">
          <div class="feature-icon">👥</div>
          <div class="feature-text">Zusammenarbeit fördern</div>
        </div>
        <div class="feature">
          <div class="feature-icon">🎯</div>
          <div class="feature-text">Ziele erreichen</div>
        </div>
      </div>
    </div>

    <!-- Buttons to navigate to todos and assignees -->
    <div class="button-container">
      <router-link to="/todos" class="start-button">
        <span class="button-icon">📝</span>
        <span>Zu den Todos</span>
      </router-link>
      <router-link to="/assignees" class="start-button">
        <span class="button-icon">👥</span>
        <span>Zu den Assignees</span>
      </router-link>
      <router-link to="/create-todos" class="start-button create-button">
        <span class="button-icon">➕</span>
        <span>Neues Todo erstellen</span>
      </router-link>
    </div>

    <!-- Footer with legal information -->
    <footer class="footer">
      <p>© 2024 Todo-App. Alle Rechte vorbehalten.</p>
      <div class="footer-links">
        <a href="/impressum" class="footer-link">Impressum</a> |
        <a href="/datenschutz" class="footer-link">Datenschutz</a> |
        <a href="/kontakt" class="footer-link">Kontakt</a>
      </div>
    </footer>
  </div>
</template>

<style scoped>
.start-page {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  min-height: 100vh;
  background: var(--color-background);
  color: #e0e0e0;
  font-family: Arial, sans-serif;
  text-align: center;
  padding: 40px 20px;
}

.welcome-container {
  margin-top: 30px;
  max-width: 800px;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.logo {
  font-size: 3rem;
  width: 80px;
  height: 80px;
  background-color: #15afce;
  color: black;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 10px rgba(33, 150, 243, 0.4);
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: #15afce;
  text-shadow: 0 0 10px rgba(33, 150, 243, 0.3);
}

.tagline {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  color: #b0b0b0;
}

.features-container {
  display: flex;
  justify-content: space-around;
  margin: 40px 0;
  flex-wrap: wrap;
  gap: 20px;
}

.feature {
  flex: 1;
  min-width: 200px;
  padding: 20px;
  background-color: #333;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.feature:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(33, 150, 243, 0.2);
}

.feature-icon {
  font-size: 2rem;
  margin-bottom: 10px;
}

.feature-text {
  font-size: 1rem;
  font-weight: bold;
  color: #e0e0e0;
}

.button-container {
  display: flex;
  gap: 15px;
  margin: 40px 0;
  flex-wrap: wrap;
  justify-content: center;
}

.start-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.8rem 1.5rem;
  background-color: #0dcaf0;
  color: black;
  text-decoration: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  transition: all 0.3s ease;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.3);
}

.start-button:hover {
  background-color: #15afce;
  transform: translateY(-3px);
  box-shadow: 0px 6px 12px rgba(33, 150, 243, 0.4);
}

.create-button {
  background-color: #198754;
  color: white;
}

.create-button:hover {
  background-color: #147548;
}

.button-icon {
  margin-right: 8px;
  font-size: 1.3rem;
}

.footer {
  text-align: center;
  margin-top: auto;
  padding: 20px;
  width: 100%;
  background-color: #333;
  border-radius: 10px 10px 0 0;
}

.footer p {
  font-size: 0.8rem;
  color: #808080;
  margin-bottom: 5px;
}

.footer-links {
  font-size: 0.8rem;
}

.footer-link {
  color: #64b5f6;
  text-decoration: none;
  margin: 0 5px;
}

.footer-link:hover {
  text-decoration: underline;
  color: #2196f3;
}

@media (max-width: 768px) {
  .button-container {
    flex-direction: column;
    width: 100%;
    max-width: 300px;
  }
  
  .start-button {
    width: 100%;
  }
  
  .features-container {
    flex-direction: column;
  }
  
  .feature {
    margin-bottom: 15px;
  }
}
</style>