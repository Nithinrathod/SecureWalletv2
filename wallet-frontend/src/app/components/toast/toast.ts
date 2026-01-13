import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.html',
  styleUrls: ['./toast.css']
})
export class ToastComponent implements OnInit {
  message = '';
  private timeoutId: any;

  constructor(private toastService: ToastService) {}

  ngOnInit() {
    this.toastService.message$.subscribe(msg => {
      this.message = msg;
      
      if (msg) {
        if (this.timeoutId) clearTimeout(this.timeoutId);
        
        // 1. Get Native Telugu Text (Processed)
        const nativeText = this.getNativeTeluguText(msg);

        // 2. Speak the processed text
        this.speak(nativeText);
      }
    });
  }

  close() {
    this.toastService.clear();
    if ('speechSynthesis' in window) {
      window.speechSynthesis.cancel();
    }
  }

  getNativeTeluguText(text: string): string {
    // Clean text to make parsing easier
    let cleanText = text.replace(/₹/g, ' ').replace(/:/g, ' ').replace(/,/g, '').trim();

    // Try to find the Transaction Amount (First number that appears)
    // We explicitly ignore "Balance" numbers by handling them separately
    const amountMatch = cleanText.match(/(\d+(\.\d+)?)/);
    const amount = amountMatch ? Math.round(parseFloat(amountMatch[0])) : 0;

    // Optional: Check for Balance at the end
    const balanceMatch = cleanText.match(/Balance\s*(\d+(\.\d+)?)/i);
    const balanceText = balanceMatch ? `. Mee balance, ${Math.round(parseFloat(balanceMatch[1]))} rupaayalu.` : '';

    // 🟢 SCENARIO 1: RECEIVED (Keep Sender Name)
    // Matches "Credited" or "Received"
    if (cleanText.match(/Credited|Received/i)) {
      const senderMatch = cleanText.match(/from\s+(.+?)(?:\s+Balance|$)/i);
      const sender = senderMatch ? senderMatch[1].trim() : "Okharu";
      return `${sender} nundi, ${amount} rupaayalu, jama ayyayi${balanceText}`;
    }

    // 🔴 SCENARIO 2: SENT (STRICT: NO RECEIVER NAME)
    // Matches "Debited" or "Sent"
    if (cleanText.match(/Debited|Sent/i)) {
      // We purposefully DO NOT look for "to User...". We just use the amount.
      return `${amount} rupaayalu, vijayavantham-gaa pampaaru${balanceText}`;
    }

    // 🔵 SCENARIO 3: ADDED
    if (cleanText.match(/Added/i)) {
      return `Mee wallet lo, ${amount} rupaayalu, add chey-a-baddayi${balanceText}`;
    }

    // ⚠️ FALLBACKS
    if (cleanText.includes("Insufficient")) return "Mee account lo saripada dabbulu levu.";
    if (cleanText.includes("Blocked")) return "Ee transaction aapi-vey-a-badindhi.";
    if (cleanText.includes("Failed")) return "Transaction viphalam ayindhi.";

    // Last Resort: Just read numbers + rupaayalu
    return cleanText.replace(/(\d+)/g, "$1 rupaayalu"); 
  }

  speak(text: string) {
    if ('speechSynthesis' in window) {
      window.speechSynthesis.cancel();

      const utterance = new SpeechSynthesisUtterance(text);
      utterance.lang = 'te-IN'; 
      utterance.rate = 0.85; 

      utterance.onend = () => {
        this.timeoutId = setTimeout(() => this.close(), 1000);
      };

      // Safety timeout
      this.timeoutId = setTimeout(() => this.close(), 10000);

      window.speechSynthesis.speak(utterance);
    } else {
      this.timeoutId = setTimeout(() => this.close(), 5000);
    }
  }
}