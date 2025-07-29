Para copiar um commit de uma branch para outra no Git, você pode usar o comando `cherry-pick`. Aqui estão os passos:

1. **Identifique o hash do commit** que você deseja copiar. Você pode usar `git log` para ver o histórico de commits e encontrar o hash do commit.

2. **Mude para a branch de destino** onde você quer aplicar o commit:
   ```sh
   git checkout <branch_destino>
   ```

3. **Aplique o commit na branch de destino** usando `cherry-pick`:
   ```sh
   git cherry-pick <commit_hash>
   ```
   Substitua `<commit_hash>` pelo hash do commit que você deseja copiar.

### Exemplo Completo:

1. **Verifique o histórico de commits** na branch de origem:
   ```sh
   git log
   ```

2. **Mude para a branch de destino**:
   ```sh
   git checkout minha-branch-destino
   ```

3. **Copie o commit**:
   ```sh
   git cherry-pick abc1234
   ```
   (onde `abc1234` é o hash do commit que você quer copiar).

Se precisar de mais alguma coisa ou tiver dúvidas, estou aqui para ajudar! 😊