<template>
  <div class="start-page">
    <div class="welcome-container">
      <h1>Willkommen bei der Todo-App!</h1>
      <p>Organisiere Todos effizient und weise sie den richtigen Assignees zu.</p>
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
    </div>

    <!-- Footer with legal information -->
    <footer class="footer">
      <p>© 2024 Todo-App. Alle Rechte vorbehalten.</p>
      <a href="/impressum" class="footer-link">Impressum</a> |
      <a href="/datenschutz" class="footer-link">Datenschutz</a>
    </footer>
  </div>
</template>

<style scoped>
.start-page {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  height: 100vh;
  background-size: cover;
  background-position: center;
  color: #ffffff;
  font-family: Arial, sans-serif;
  text-align: center;
  padding: 20px;
}

.welcome-container {
  margin-top: 50px;
}

h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
}

p {
  font-size: 1rem;
  margin-bottom: 1rem;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.7);
}

.button-container {
  display: flex;
  gap: 20px;
}

.start-button {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.7rem 1.4rem;
  background-color: #1476ae;
  color: #fff;
  text-decoration: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: bold;
  transition: all 0.3s ease;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.2);
}

.start-button:hover {
  background-color: #5886a1;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.3);
}

.button-icon {
  margin-right: 8px;
  font-size: 1.3rem;
}

.footer {
  text-align: center;
  margin-top: auto;
  padding: 4px;
  font-size: 0.7rem;
  color: #cccccc;
}

.footer-link {
  color: #cccccc;
  text-decoration: none;
  margin: 0 5px;
}

.footer-link:hover {
  text-decoration: underline;
}
</style>
