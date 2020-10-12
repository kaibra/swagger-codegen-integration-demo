import Reveal from 'reveal.js/dist/reveal';
import "reveal.js/dist/reveal.css"
import "reveal.js/dist/theme/black.css"
import RevealHighlight from "reveal.js/plugin/highlight/highlight.esm.js"
import 'reveal.js/plugin/highlight/monokai.css';
import Markdown from 'reveal.js/plugin/markdown/markdown.esm.js';

let deck = new Reveal({
    history: true,
    plugins: [Markdown, RevealHighlight]
})

deck.initialize();