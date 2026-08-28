# 3grauineitor
 
Calculadora de equações do 3º grau para Android, escrita em Kotlin com Jetpack Compose.
 
Nasceu de um desafio proposto por um professor de Cálculo: resolver a forma geral
**ax³ + bx² + cx + d = 0** — não por aproximação numérica, mas pela solução algébrica exata.

Logica originalmente criada na linguagem C
 
---
 
## O que faz
 
Recebe os quatro coeficientes e devolve as **três raízes** da equação, reais ou complexas,
com quatro casas decimais. A fórmula é montada na tela conforme você digita, então dá para
conferir a equação antes de resolver.
 
---
 
## Como resolve: o método de Cardano
 
O app não itera nem chuta. Implementa a fórmula de Cardano, em quatro passos:
 
**1. Normalização.** Divide todos os coeficientes por `a`, reduzindo a equação à forma mônica.
 
**2. Substituição de Tschirnhaus.** Aplica um deslocamento de `a/3` para eliminar o termo
quadrático, transformando a equação na forma deprimida **t³ + pt + q = 0** — que é a forma
que Cardano sabe resolver.
 
**3. Discriminante.** Calcula `Δ = q²/4 + p³/27`. O sinal de Δ determina a natureza das raízes.
 
**4. Resolução, em dois ramos:**
 
| Caso | Natureza das raízes | Tratamento |
|---|---|---|
| **Δ > 0** | uma raiz real e um par de complexas conjugadas | raízes cúbicas diretas; retorna `x₁` real e `x₂ ± x₂ᵢ·i` |
| **Δ ≤ 0** | três raízes reais distintas | forma trigonométrica: `x = 2·∛r·cos(φ/3 + 2πk/3)`, com k = 0, 1, 2 |
 
O segundo ramo existe por um motivo específico: quando as três raízes são reais, a fórmula
algébrica de Cardano passa obrigatoriamente por números complexos intermediários — o chamado
*casus irreducibilis*. A forma trigonométrica contorna isso e devolve as três raízes reais
sem aritmética complexa.
 
---
 
## Stack
 
| | |
|---|---|
| Linguagem | Kotlin |
| UI | Jetpack Compose (Material 3) |
| Arquitetura | single-activity, composables sem estado externo |
| `minSdk` | 24 (Android 7.0) |
| `targetSdk` / `compileSdk` | 36 |
| Build | Gradle Kotlin DSL |
 
### Composables
 
- **`CubicSolverScreen`** — tela principal: título, fórmula dinâmica, campos, botão e card de resultado.
- **`CoefficientInput`** — campo reutilizável com teclado numérico, um por coeficiente.
- **`solveCubic()`** — a lógica de resolução, isolada da camada de UI.
- **`cbrt()`** — helper de raiz cúbica com sinal, necessário porque `Math.cbrt` e potências
  fracionárias tratam números negativos de forma inconsistente.
---
 
## Instalando
 
**Direto no celular:** baixe o `instalador.apk` da raiz do repositório e instale
(é preciso permitir instalação de fontes desconhecidas). Requer Android 7.0 ou superior.
 
**Compilando:**
```bash
git clone https://github.com/Kaykyduken/3grauineitor.git
cd 3grauineitor
./gradlew assembleDebug
```
Ou abra a pasta no Android Studio e rode direto.
 
---
 
## Autor
 
**Kayky Gabriel Silvano Tomé** — Análise e Desenvolvimento de Sistemas, FATEC Itapetininga

[github.com/Kaykyduken](https://github.com/Kaykyduken)
