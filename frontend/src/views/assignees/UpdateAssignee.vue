<script setup lang="ts">
/**
 * This component allows updating an existing assignee.
 */

import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast, Toast } from '../../ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import { fetchAssignee } from '../../ts/Assignee'
import type { Assignee } from '../../ts/Assignee'
import config from '../../config'
import '@/assets/buttons.css'
import '@/assets/input-group.css'

const router = useRouter()
const route = useRoute()

const id = Number(route.params.id)

// assignee attributes
const prename = ref('')
const name = ref('')
const email = ref('')

// Error message 
const errorMessage = ref('')

/**
 * Fetch assignee by id on mount
 */
onMounted(() => {
  fetchAssignee(id)
    .then((data: Assignee) => {
      console.log('Fetched Assignee:', data)
      prename.value = data.prename
      name.value = data.name
      email.value = data.email
    })
    .catch((error) => {
      console.error('Error fetching assignee:', error.message)
      showToast(
        new Toast('Error', `Fehler beim Laden des Assignees: ${error.message}`, 'error', faCheck, 5)
      )
    })
})

/**
 * Update assignee data
 */
function updateAssignee(event: Event) {
  event.preventDefault()

  errorMessage.value = '' 

  fetch(`${config.apiBaseUrl}/assignees/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      prename: prename.value,
      name: name.value,
      email: email.value
    })
  })
    .then((response) => {
      if (!response.ok) {
        return response.json().then((error) => {
          throw new Error(error.message) 
        })
      }
      return response.json()
    })
    .then((data) => {
      console.log('Updated Assignee:', data)
      showToast(new Toast('Success', 'Assignee erfolgreich aktualisiert!', 'success', faCheck, 5))
      router.push('/assignees')
    })
    .catch((error) => {
      console.error(error)
      showToast(new Toast('Error', `Fehler beim Aktualisieren des Assignees: ${error.message}`, 'error', faXmark, 10))
    })
}

/**
 * Navigate back to assignees view
 */
const goBack = () => {
  router.push('/assignees')
}
</script>

<template>
  <form @submit="updateAssignee">
     <!-- container for updating assignee -->
    <h1>Assignee bearbeiten</h1>
    <div class="input-group">
      <label for="prename">Vorname</label>
      <input type="text" id="prename" v-model="prename" required />
    </div>
    <div class="input-group">
      <label for="name">Nachname</label>
      <input type="text" id="name" v-model="name" required />
    </div>
    <div class="input-group">
      <label for="email">Email</label>
      <input type="email" id="email" v-model="email" required />
    </div>
    <button type="submit" class="update-button">Assignee aktualisieren</button>
    <button @click="goBack" type="button" class="cancel-button">Abbrechen</button>
  </form>
</template>

<style scoped>
.container {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f9f9f9;
}

form {
  max-width: 500px;
  width: 100%;
  background: #000000;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h1 {
  margin-bottom: 20px;
  font-size: 24px;
  text-align: center;
  color: #ffffff;
}

</style>
