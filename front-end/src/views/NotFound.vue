<template>
    <div class="container flex flex-col justify-center items-center gap-2 max-w-full h-96 m-auto">
        <h1 class="text-3xl bold">Helaas... deze pagina bestaat niet (meer)?</h1>
        <RouterLink to="/"><button class="themed-button">Ga terug naar de home</button></RouterLink>
    </div>
</template>

<script>
export default {
    name: 'NotFound',
}
</script>