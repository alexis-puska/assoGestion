import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class StringUtilsService {
  /**
   * Insère dans le param 'str' des highlights (des balises <strong>) autour du terme 'term' spécifié.
   * Ignore la casse.
   * @param str le string à modifier
   * @param term le terme à highlight
   * @returns le string résultant de la modification.
   */
  highlightTermInString(str: string, term: string): string {
    if (!str?.trim()) {
      return '';
    }
    if (!term?.trim()) {
      return str;
    }

    const lowerCase = str.trim().toLocaleLowerCase();
    const searchLowerCase = term.trim().toLocaleLowerCase();

    // Connaître les positions où highlight
    const indices: number[] = [];
    for (let pos = lowerCase.indexOf(searchLowerCase); pos !== -1; pos = lowerCase.indexOf(searchLowerCase, pos + 1)) {
      indices.push(pos);
    }

    if (indices.length) {
      const final = [];
      let lastIndex = 0;
      indices.forEach((index, loopNumber) => {
        // garder ce qu'il y a avant le dernier highlight (ou le début du string si jamais highlight)
        if (loopNumber > 0) {
          final.push(str.substring(indices[loopNumber - 1] + term.length, index));
        } else {
          final.push(str.substring(0, indices[0]));
        }
        // highlight
        final.push('<strong>');
        final.push(str.substring(index, index + term.length));
        final.push('</strong>');
        // se souvenir de l'index de fin du dernier highlight pour pouvoir compléter le string
        lastIndex = index + term.length;
      });
      // compléter le string
      final.push(str.substring(lastIndex));
      return final.join('');
    }

    return str;
  }

  /**
   * Return truncated str with ellipsis if length greater than charNumber.
   */
  getStrTruncated(str: string, charNumber: number): string {
    return str.length < charNumber ? str : str.slice(0, charNumber) + '…';
  }
}
