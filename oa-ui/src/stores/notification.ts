import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const unreadCount = ref(5)

  function setCount(count: number) {
    unreadCount.value = count
  }

  function decrement() {
    if (unreadCount.value > 0) unreadCount.value--
  }

  return { unreadCount, setCount, decrement }
})
