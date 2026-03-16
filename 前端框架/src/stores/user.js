import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
 state: () => ({
  userID: null
 }),
 actions: {
  setUserID(id) {
   this.userID = id
  }
 }
})