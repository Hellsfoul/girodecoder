<template>
  <div class="result-container" v-if="result">
    <h2>EPC-QR Code Decoded</h2>
    
    <!-- Primary Fields Section -->
    <div class="section">
      <h3>Primary Information</h3>
      <div class="grid">
        <div class="field" v-if="result.serviceTag">
          <strong>Service Tag:</strong>
          <span>{{ result.serviceTag }}</span>
        </div>
        <div class="field" v-if="result.version">
          <strong>Version:</strong>
          <span>{{ result.version }}</span>
        </div>
        <div class="field" v-if="result.characterSet">
          <strong>Character Set:</strong>
          <span>{{ result.characterSet }}</span>
        </div>
        <div class="field" v-if="result.identification">
          <strong>Identification:</strong>
          <span>{{ result.identification }}</span>
        </div>
      </div>
    </div>

    <!-- Banking Information Section -->
    <div class="section">
      <h3>Banking Information</h3>
      <div class="grid">
        <div class="field" v-if="result.bic">
          <strong>BIC:</strong>
          <span>{{ result.bic }}</span>
        </div>
        <div class="field" v-if="result.iban">
          <strong>IBAN:</strong>
          <span class="monospace">{{ result.iban }}</span>
        </div>
        <div class="field" v-if="result.name">
          <strong>Recipient Name:</strong>
          <span>{{ result.name }}</span>
        </div>
      </div>
    </div>

    <!-- Payment Information Section -->
    <div class="section">
      <h3>Payment Information</h3>
      <div class="grid">
        <div class="field" v-if="result.amountCurrency || result.amountValue">
          <strong>Amount:</strong>
          <span>{{ result.amountCurrency || '—' }} {{ result.amountValue || '—' }}</span>
        </div>
        <div class="field" v-if="result.purpose">
          <strong>Purpose:</strong>
          <span>{{ result.purpose }}</span>
        </div>
      </div>
    </div>

    <!-- Remittance & Information Section -->
    <div class="section">
      <h3>Payment Details</h3>
      <div class="grid">
        <div class="field full-width" v-if="result.remittance">
          <strong>Remittance:</strong>
          <span>{{ result.remittance }}</span>
        </div>
        <div class="field full-width" v-if="result.information">
          <strong>Information:</strong>
          <span>{{ result.information }}</span>
        </div>
      </div>
    </div>

    <!-- Raw Data Section -->
    <div class="section raw-data">
      <h3>Raw QR Text</h3>
      <pre>{{ result.rawText }}</pre>
    </div>

    <!-- Extras Section -->
    <div class="section" v-if="Object.keys(result.extras).length > 0">
      <h3>Additional Fields</h3>
      <div class="grid">
        <div class="field" v-for="(value, key) in result.extras" :key="key">
          <strong>{{ key }}:</strong>
          <span>{{ value }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ResultDisplay',
  props: {
    result: {
      type: Object,
      default: null
    }
  }
}
</script>

<style scoped>
.result-container {
  max-width: 900px;
  margin: 30px auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.result-container h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.section {
  margin-bottom: 25px;
  background: white;
  padding: 15px;
  border-radius: 6px;
  border-left: 4px solid #42b983;
}

.section h3 {
  color: #42b983;
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
}

.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 15px;
}

.field {
  display: flex;
  flex-direction: column;
}

.field.full-width {
  grid-column: 1 / -1;
}

.field strong {
  color: #555;
  font-weight: 600;
  margin-bottom: 5px;
  font-size: 14px;
}

.field span {
  color: #333;
  word-break: break-word;
  padding: 8px;
  background: #f5f5f5;
  border-radius: 4px;
  font-size: 14px;
}

.field span.monospace {
  font-family: 'Courier New', monospace;
  font-size: 13px;
}

.raw-data {
  border-left-color: #ff7043;
}

.raw-data h3 {
  color: #ff7043;
}

.raw-data pre {
  background: #f5f5f5;
  padding: 12px;
  border-radius: 4px;
  overflow-x: auto;
  font-size: 12px;
  line-height: 1.4;
  max-height: 200px;
  overflow-y: auto;
  margin: 0;
}
</style>
