<template>
  <div id="app">
    <header class="app-header">
      <div class="container">
        <h1>EPC-QR Code Decoder</h1>
        <p>Upload an EPC-QR code image to extract payment information</p>
      </div>
    </header>

    <main class="container">
      <ImageDropZone @image-uploaded="handleImageUploaded" />
      <ResultDisplay :result="decodedResult" />
    </main>

    <footer class="app-footer">
      <p>&copy; 2025 EPC-QR Decoder</p>
    </footer>
  </div>
</template>

<script>
import ImageDropZone from './components/ImageDropZone.vue'
import ResultDisplay from './components/ResultDisplay.vue'

export default {
  name: 'App',
  components: {
    ImageDropZone,
    ResultDisplay
  },
  data() {
    return {
      decodedResult: null
    }
  },
  methods: {
    handleImageUploaded(result) {
      this.decodedResult = result
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  background: #fafafa;
  color: #333;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.app-header {
  background: linear-gradient(135deg, #42b983 0%, #359968 100%);
  color: white;
  padding: 40px 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.app-header h1 {
  font-size: 32px;
  margin-bottom: 10px;
}

.app-header p {
  font-size: 16px;
  opacity: 0.9;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
  width: 100%;
}

main {
  flex: 1;
  padding: 40px 20px;
}

.app-footer {
  background: #333;
  color: white;
  text-align: center;
  padding: 20px;
  margin-top: 40px;
}

.app-footer p {
  font-size: 14px;
  opacity: 0.7;
}

@media (max-width: 768px) {
  .app-header h1 {
    font-size: 24px;
  }

  .app-header p {
    font-size: 14px;
  }

  main {
    padding: 20px 10px;
  }
}
</style>
