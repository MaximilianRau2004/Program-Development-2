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

// error message for validation
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
      showToast(new Toast('Success', `Erfolgreich Assignee erstellt!`, 'success', faCheck, 5))
      router.push('/assignees')
    })
    .catch((error) => {
      console.error(error)
      showToast(new Toast('Error', `Fehler beim Erstellen des Assignees: ${error.message}`, 'error', faXmark, 10))
    })
}
</script>

<template>
  <div class="container">
    <!-- container for creating an assignee -->
    <div class="create assignee">
      <h2 class="assignee-header">Neuen Assignee erstellen</h2>
      <form @submit.prevent="createAssignee">
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
        <Button @click="createAssignee" type="button" class="create-button"
          >Assignee erstellen</Button
        >
      </form>
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
