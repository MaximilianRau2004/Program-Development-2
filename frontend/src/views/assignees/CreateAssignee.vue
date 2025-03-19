<script setup lang="ts">
/**
 * This component allows the creation of a new assignee.
 * It includes a form for entering the attirbutes of the assignee.
 */
import { Button } from 'agnostic-vue'
import config from '../../config'
import { showToast, Toast } from '../../ts/toasts'
import { faCheck, faXmark } from '@fortawesome/free-solid-svg-icons'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import '@/assets/buttons.css'
import '@/assets/input-group.css'

const router = useRouter()

// assignee attributes
const prename = ref('')
const name = ref('')
const email = ref('')

const errorMessage = ref('')

/**
 * create a new assignee
 */
function createAssignee() {
  errorMessage.value = ''
  fetch(`${config.apiBaseUrl}/assignees`, {
    method: 'POST',
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
    .then(() => {
      showToast(new Toast('Erfolg', `Erfolgreich Assignee erstellt!`, 'success', faCheck, 5))
      router.push('/assignees')
    })
    .catch((error) => {
      console.error(error)
      showToast(new Toast('Fehler', `Fehler beim Erstellen des Assignees: ${error.message}`, 'error', faXmark, 5))
    })
}
</script>

<template>
  <div class="container p-4 bg-dark text-light rounded">
    <div class="mb-3">
      <h5 class="mb-4">Assignee erstellen</h5>
    </div>
    <div class="mb-4">
      <div class="mb-3">
        <label for="editPrename" class="form-label">Vorname</label>
        <input
          type="text"
          class="form-control bg-dark text-light border-secondary w-100"
          id="editPrename"
          v-model="prename"
          required
        />
      </div>
      <div class="mb-3">
        <label for="editName" class="form-label">Name</label>
        <input
          type="text"
          class="form-control bg-dark text-light border-secondary w-100"
          id="editName"
          v-model="name"
          required
        />
      </div>
      <div class="mb-3">
        <label for="editEmail" class="form-label">Email</label>
        <input
          type="email"
          class="form-control bg-dark text-light border-secondary w-100"
          id="editEmail"
          v-model="email"
          required
        />
      </div>
      <!-- Add more fields here if needed -->
    </div>
    <div class="d-flex justify-content-end">
      <button @click="createAssignee" type="button" class="btn btn-info">Erstellen</button>
    </div>
  </div>
</template>

<style scoped>
.container {
  padding: 20px;
  background-color: #000000;
  color: #ffffff;
  border: 1px solid #ffffff;
  border-radius: 8px;
  max-width: 600px;
  margin: 0 auto;
}
</style>
