import axios from 'axios'

const API_BASE_URL = '/api'

export const epcApi = {
  decodeImage: (file) => {
    const formData = new FormData()
    formData.append('file', file)
    return axios.post(`${API_BASE_URL}/epc/decode`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  decodeImageRaw: (file) => {
    return axios.post(`${API_BASE_URL}/epc/decode`, file, {
      headers: { 'Content-Type': file.type || 'application/octet-stream' }
    })
  }
}
