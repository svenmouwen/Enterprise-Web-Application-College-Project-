<template>
    <div class="flex flex-col items-center justify-between w-full p-2">
        <div class="flex flex-col items-center justify-center w-full min-h-[360px] scanner-wrapper"
            id="qr-code-full-region"></div>
    </div>
</template>

<script>
import { Html5QrcodeScanner } from "html5-qrcode";

export default {
    data() {
        return {
            html5QrcodeScanner: null,
        };
    },
    mounted() {
        const config = {
            fps: 10,
            qrbox: 250,
        };
        this.html5QrcodeScanner = new Html5QrcodeScanner('qr-code-full-region', config);
        this.html5QrcodeScanner.render(this.onScanSuccess);

        const qrScanner = document.querySelector('.scanner-wrapper');
        qrScanner.firstElementChild.firstElementChild.remove();
        qrScanner.firstElementChild.firstElementChild.remove();
    },
    methods: {
        onScanSuccess(decodedText, decodedResult) {
            this.$emit('result', decodedText, decodedResult);
            setTimeout(() => {
                this.html5QrcodeScanner.clear();
            }, 500);
        },
    },
};
</script>

<style>
a:hover {
    cursor: pointer;
}

.scanner-wrapper {
    margin: auto;
    border-radius: 20px;
}

.scanner-wrapper div {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
}

#html5-qrcode-button-camera-start,
#html5-qrcode-button-camera-stop,
#html5-qrcode-select-camera {
    border: 1px solid var(--border);
    padding: 5px 10px;
    border-radius: 5px;
    margin: 10px 0 0 0;
    width: 100%;
    max-width: 35vh;
    border-radius: 10px;
}

#html5-qrcode-select-camera {
    padding: 5px 5px;
    text-align: center;
}

#qr-code-full-region__dashboard_section_csr span {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin: 0 10px;
}

#qr-code-full-region__header_message {
    border-radius: 15px;
    padding: 10px 10px;
    margin: 10px;
    width: 87%;
}

#qr-code-full-region__dashboard_section_csr+div {
    width: 90%;
}

#qr-canvas-visible {
    border-radius: 10px;
}

#html5-qrcode-button-camera-permission {
    text-decoration: underline;
}

span.html5-qrcode-element {
    border: 1px solid var(--border);
    border-radius: 10px;
    padding: 10px;
    margin: 10px 0;
    box-sizing: border-box;
    width: 100%;
    max-width: 35vh;
}
</style>
