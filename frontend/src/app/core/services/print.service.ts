import { Service } from '@angular/core';

@Service()
export class PrintService {

    private readonly colores = {
        principal: '#1A3A6B',
        secundario: '#F4F6FA',
        tercero: '#F5A623',
        paginaPrincipal: '#F4F6FA',
        textoPrimario: '#1A3A6B',
        textoSecundario: '#5A6D8C',
    };

    printJsonAsTable(
        data: Record<string, unknown>[],
        title = 'Reporte oficial de administradores',
        empresa?: any,
        columns?: string[],
    ): void {
        if (!data || data.length === 0) return;

        const cols = columns ?? Object.keys(data[0]);

        const headerCells = cols
            .map((col) => `<th>${this.escapeHtml(this.formatHeader(col))}</th>`)
            .join('');

        const bodyRows = data
            .map((row) => {
                const cells = cols
                    .map((col) => `<td>${this.escapeHtml(this.formatValue(row[col]))}</td>`)
                    .join('');
                return `<tr>${cells}</tr>`;
            })
            .join('');

        const fecha = new Date().toLocaleDateString('es-PE', {
            day: '2-digit',
            month: 'long',
            year: 'numeric',
        });
        const hora = new Date().toLocaleTimeString('es-PE', {
            hour: '2-digit',
            minute: '2-digit',
        });

        const c = this.colores;
        const nombreEmpresa = empresa?.name || 'Colegio San Andrés';
        const subtituloEmpresa = 'Sistema de Gestión Educativa';
        const logoUrl = empresa?.logoUrl ?? null;

        const logoBandaHtml = logoUrl
            ? `<img src="${this.escapeHtml(logoUrl)}" alt="logo" class="logo-banda" />`
            : `<div class="logo-banda logo-banda--placeholder"></div>`;

        const logoFooterHtml = logoUrl
            ? `<img src="${this.escapeHtml(logoUrl)}" alt="logo" class="logo-footer" />`
            : '';

        const watermarkHtml = logoUrl
            ? `<img src="${this.escapeHtml(logoUrl)}" alt="" class="watermark" />`
            : '';

        const html = `
      <!DOCTYPE html>
      <html lang="es">
      <head>
        <meta charset="UTF-8" />
        <title>${this.escapeHtml(nombreEmpresa)}</title>
        <style>
          * { box-sizing: border-box; }
          html, body {
            background: ${c.paginaPrincipal};
            -webkit-print-color-adjust: exact;
            print-color-adjust: exact;
            color-adjust: exact;
          }
          body {
            font-family: Helvetica, Arial, sans-serif;
            color: ${c.textoPrimario};
            margin: 0;
            position: relative;
          }
          .watermark {
            position: fixed;
            top: 50%;
            left: 50%;
            width: 320px;
            height: 320px;
            object-fit: contain;
            transform: translate(-50%, -50%);
            opacity: 0.05;
            pointer-events: none;
            z-index: 0;
          }

          .banda {
            background: ${c.principal};
            border-bottom: 3px solid ${c.tercero};
            padding: 16px 32px;
            display: flex;
            align-items: center;
            gap: 14px;
          }
          .logo-banda {
            width: 46px;
            height: 46px;
            object-fit: contain;
            border-radius: 50%;
            background: #ffffff;
            padding: 5px;
            flex-shrink: 0;
          }
          .logo-banda--placeholder {
            background: rgba(255,255,255,0.15);
          }
          .banda-texto h1 {
            margin: 0;
            font-size: 16px;
            font-weight: 700;
            color: #ffffff;
          }
          .banda-texto p {
            margin: 2px 0 0 0;
            font-size: 9.5px;
            color: #d9c9a0;
          }

          .contenido {
            position: relative;
            z-index: 1;
            padding: 26px 32px 90px 32px;
          }
          .eyebrow {
            font-size: 8.5px;
            font-weight: 700;
            letter-spacing: 0.5px;
            color: #b8892f;
            text-transform: uppercase;
            margin: 0 0 6px 0;
          }
          h2.titulo {
            font-size: 19px;
            font-weight: 700;
            color: #262a36;
            margin: 0 0 4px 0;
          }
          .fecha {
            font-size: 9px;
            color: ${c.textoSecundario};
            font-style: italic;
            margin: 0 0 12px 0;
          }
          .linea-divisoria {
            height: 2px;
            background: linear-gradient(to right, ${c.tercero} 6%, ${c.secundario} 6%);
            margin-bottom: 10px;
          }
          .total-registros {
            font-size: 9px;
            color: ${c.textoSecundario};
            margin: 0 0 12px 0;
          }
          table {
            width: 100%;
            border-collapse: collapse;
            font-size: 9.5px;
            background: #ffffff;
          }
          thead th {
            background: ${c.principal};
            color: #ffffff;
            text-align: left;
            padding: 8px 8px;
            border-bottom: 2px solid ${c.tercero};
            text-transform: uppercase;
            font-size: 8px;
            letter-spacing: 0.3px;
          }
          tbody td {
            padding: 7px 8px;
            border-bottom: 1px solid ${c.secundario};
            color: #262a36;
          }
          tbody tr:nth-child(even) {
            background: ${c.secundario};
          }
 
          .footer {
            position: fixed;
            left: 0;
            right: 0;
            bottom: 0;
            background: ${c.paginaPrincipal};
            border-top: 1px solid ${c.secundario};
            padding: 8px 32px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            font-size: 7.5px;
            color: ${c.textoSecundario};
            z-index: 2;
          }
          .footer-izq {
            display: flex;
            align-items: center;
            gap: 6px;
          }
          .logo-footer {
            width: 13px;
            height: 13px;
            object-fit: contain;
          }
          .footer-centro {
            font-style: italic;
          }
 
          @media print {
            @page { margin: 0; }
            .footer { position: fixed; }
            * {
              -webkit-print-color-adjust: exact !important;
              print-color-adjust: exact !important;
              color-adjust: exact !important;
            }
          }
        </style>
      </head>
      <body>
        ${watermarkHtml}
 
        <div class="banda">
          ${logoBandaHtml}
          <div class="banda-texto">
            <h1>${this.escapeHtml(nombreEmpresa)}</h1>
            <p>${this.escapeHtml(subtituloEmpresa)}</p>
          </div>
        </div>
 
        <div class="contenido">
          <p class="eyebrow">Reporte oficial</p>
          <h2 class="titulo">${this.escapeHtml(title)}</h2>
          <p class="fecha">Generado el ${fecha}, ${hora}</p>
          <div class="linea-divisoria"></div>
          <p class="total-registros">Total de registros: ${data.length}</p>
 
          <table>
            <thead><tr>${headerCells}</tr></thead>
            <tbody>${bodyRows}</tbody>
          </table>
        </div>
 
        <div class="footer">
          <div class="footer-izq">
            ${logoFooterHtml}
            <span>${this.escapeHtml(nombreEmpresa)}</span>
          </div>
          <span class="footer-centro">Documento generado automáticamente — uso interno</span>
          <span></span>
        </div>
      </body>
      </html>
    `;

        const iframe = document.createElement('iframe');
        iframe.style.position = 'fixed';
        iframe.style.right = '0';
        iframe.style.bottom = '0';
        iframe.style.width = '0';
        iframe.style.height = '0';
        iframe.style.border = '0';

        document.body.appendChild(iframe);

        const doc = iframe.contentWindow?.document;
        if (!doc) {
            document.body.removeChild(iframe);
            return;
        }

        doc.open();
        doc.write(html);
        doc.close();

        iframe.onload = () => {
            setTimeout(() => {
                iframe.contentWindow?.focus();
                iframe.contentWindow?.print();
            }, 150);
        };

        setTimeout(() => document.body.removeChild(iframe), 60_000);
    }

    private formatHeader(key: string): string {

        const withSpaces = key
            .replace(/_/g, ' ')
            .replace(/([a-z0-9])([A-Z])/g, '$1 $2');
        return withSpaces.charAt(0).toUpperCase() + withSpaces.slice(1);
    }

    private formatValue(value: unknown): string {
        if (value === null || value === undefined) {
            return '—';
        }
        return String(value);
    }

    private escapeHtml(text: string): string {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    printPdfBlob(blob: Blob): void {
        if (!blob || blob.size === 0) return;

        const url = URL.createObjectURL(blob);

        const iframe = document.createElement('iframe');
        iframe.style.position = 'fixed';
        iframe.style.right = '0';
        iframe.style.bottom = '0';
        iframe.style.width = '0';
        iframe.style.height = '0';
        iframe.style.border = '0';
        iframe.src = url;

        document.body.appendChild(iframe);

        iframe.onload = () => {
            try {
                setTimeout(() => {
                    iframe.contentWindow?.focus();
                    iframe.contentWindow?.print();
                }, 200);

            } catch (error) {
                console.error('PrintService: error al intentar imprimir el PDF.', error);
            } finally {

                setTimeout(() => {
                    document.body.removeChild(iframe);
                    URL.revokeObjectURL(url);
                }, 60_000);
            }
        };
    }

    openPdfBlob(blob: Blob): void {
        if (!blob || blob.size === 0) return;

        const url = URL.createObjectURL(blob);
        const nuevaVentana = window.open(url, '_blank');
        if (!nuevaVentana) {
            console.warn('PrintService: el navegador bloqueó la ventana emergente. ' +
                'Se recomienda descargar el archivo en su lugar.');
        }
        setTimeout(() => URL.revokeObjectURL(url), 60_000);
    }

    downloadPdfBlob(blob: Blob, filename: string): void {
        if (!blob || blob.size === 0) return;

        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = filename.endsWith('.pdf') ? filename : `${filename}.pdf`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    }
}
