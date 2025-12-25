<template>
  <div class="drop-zone" @drop="handleDrop" @dragover.prevent @dragenter.prevent @dragleave="isDragOver = false" :class="{ 'drag-over': isDragOver }">
    <div class="drop-content">
      <svg class="upload-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
        <polyline points="17 8 12 3 7 8"></polyline>
        <line x1="12" y1="3" x2="12" y2="15"></line>
      </svg>
      <h2>Drop an image here</h2>
      <p>paste from clipboard, or click to select</p>
      <input type="file" @change="handleFileInput" accept="image/*" ref="fileInput" style="display: none">
      <button @click="openFileDialog" class="select-btn">Select Image</button>
    </div>
    
    <div v-if="preview" class="preview-section">
      <img :src="preview" alt="preview" class="preview-img">
      <div class="file-info">
        <p><strong>File:</strong> {{ fileName }}</p>
        <p><strong>Size:</strong> {{ fileSize }}</p>
      </div>
    </div>

    <button v-if="preview && !loading" @click="uploadImage" class="upload-btn">Upload & Decode</button>
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>Processing...</p>
    </div>
    <div v-if="error" class="error-msg">{{ error }}</div>
  </div>
</template>

<script>
import { epcApi } from '../api/epcApi'

export default {
  name: 'ImageDropZone',
  emits: ['image-uploaded'],
  data() {
    return {
      isDragOver: false,
      preview: null,
      fileName: '',
      fileSize: '',
      currentFile: null,
      loading: false,
      error: null
    }
  },
  mounted() {
    window.addEventListener('paste', this.handlePaste)
  },
  beforeUnmount() {
    window.removeEventListener('paste', this.handlePaste)
  },
  methods: {
    openFileDialog() {
      this.$refs.fileInput.click();
    },
    handleDrop(e) {
      e.preventDefault()
      this.isDragOver = false
      const files = e.dataTransfer.files
      if (files.length > 0) {
        this.processFile(files[0])
      }
    },
    handleFileInput(e) {
      const files = e.target.files
      if (files.length > 0) {
        this.processFile(files[0])
      }
    },
    handlePaste(e) {
      const items = e.clipboardData.items
      for (let i = 0; i < items.length; i++) {
        const item = items[i]
        if (item.type.startsWith('image/')) {
          const file = item.getAsFile()
          this.processFile(file)
          break // Only process the first image
        }
      }
    },
    processFile(file) {
      if (!file.type.startsWith('image/')) {
        this.error = 'Please drop an image file'
        return
      }
      this.error = null
      this.currentFile = file
      this.fileName = file.name || 'Pasted Image'
      this.fileSize = (file.size / 1024).toFixed(2) + ' KB'
      
      const reader = new FileReader()
      reader.onload = (e) => {
        this.preview = e.target.result
      }
      reader.readAsDataURL(file)
    },
    async uploadImage() {
      if (!this.currentFile) return
      this.loading = true
      this.error = null
      
      try {
        const response = await epcApi.decodeImageRaw(this.currentFile)
        this.$emit('image-uploaded', response.data)
      } catch (err) {
        this.error = err.response?.data?.error || err.message || 'Upload failed'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.drop-zone {
  max-width: 600px;
  margin: 30px auto;
  padding: 40px 20px;
  border: 2px dashed #42b983;
  border-radius: 8px;
  background: #f0f8f4;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.drop-zone.drag-over {
  background: #e0f0e8;
  border-color: #2c7d5e;
  transform: scale(1.02);
}

.drop-content {
  pointer-events: none;
}

/* Allow interactive elements inside the drop content to receive clicks */
.drop-content .select-btn,
.drop-content input {
  pointer-events: auto;
}

.upload-icon {
  width: 60px;
  height: 60px;
  color: #42b983;
  margin-bottom: 15px;
}

.drop-zone h2 {
  margin: 15px 0 5px;
  color: #333;
  font-size: 24px;
}

.drop-zone p {
  color: #666;
  font-size: 14px;
  margin: 0 0 15px;
}

.select-btn {
  background: #42b983;
  color: white;
  border: none;
  padding: 10px 30px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s;
}

.select-btn:hover {
  background: #359968;
}

.preview-section {
  margin-top: 25px;
  display: flex;
  gap: 20px;
  align-items: center;
  padding: 15px;
  background: white;
  border-radius: 6px;
}

.preview-img {
  max-width: 120px;
  max-height: 120px;
  border-radius: 4px;
  object-fit: contain;
}

.file-info {
  text-align: left;
  flex: 1;
}

.file-info p {
  margin: 5px 0;
  color: #555;
  font-size: 14px;
}

.file-info strong {
  color: #333;
}

.upload-btn {
  margin-top: 20px;
  background: #42b983;
  color: white;
  border: none;
  padding: 12px 40px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s;
}

.upload-btn:hover {
  background: #359968;
}

.loading {
  margin-top: 20px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #e0e0e0;
  border-top-color: #42b983;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin: 0 auto;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading p {
  margin-top: 10px;
  color: #42b983;
}

.error-msg {
  margin-top: 15px;
  padding: 12px;
  background: #ffebee;
  color: #c62828;
  border-radius: 4px;
  font-size: 14px;
}
</style>
