import { Component, OnInit } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { CustomerService } from '../services/customer.service';
import { AccountsService } from '../services/accounts.service';
import { forkJoin } from 'rxjs';
import { map } from 'rxjs/operators';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  totalCustomers: number = 0;
  totalAccounts: number = 0;
  totalBalance: number = 0;

  constructor(private customerService: CustomerService, private accountsService: AccountsService) { }

  ngOnInit() {
    this.loadTotals();
    this.loadCustomerAccounts();
    this.loadAccountTypes();
    this.loadOperationTypes();
  }

  loadTotals() {
    this.customerService.getCustomers().subscribe(customers => {
      this.totalCustomers = customers.length;
    });

    this.accountsService.getAccounts().subscribe(accounts => {
      this.totalAccounts = accounts.length;
      this.totalBalance = accounts.reduce((total, account) => total + account.balance, 0);
    });
  }

  loadCustomerAccounts() {
    this.customerService.getCustomers().subscribe(customers => {
      const barChartLabels = customers.map(customer => customer.name);
      const accountCounts = customers.map(customer => this.accountsService.getAccounts().pipe(
        map(accounts => accounts.filter(account => account.customerDTO.id === customer.id).length)
      ));
      forkJoin(accountCounts).subscribe(counts => {
        const barChartData = counts;
        this.renderBarChart(barChartLabels, barChartData);
      });
    });
  }

  loadAccountTypes() {
    this.accountsService.getAccounts().subscribe(accounts => {
      const currentAccountCount = accounts.filter(account => account.accountType == 'CurrentAccount').length;
      const savingAccountCount = accounts.filter(account => account.accountType == 'SavingAccount').length;

      // Ensuring we only consider two types of accounts: Current and Saving
      const pieChartData = [currentAccountCount, savingAccountCount];
      this.renderPieChart(pieChartData);
    });
  }

  loadOperationTypes() {
    this.accountsService.getAccounts().subscribe(accounts => {
      let debitCount = 14;
      let creditCount = 10;
      accounts.forEach(account => {
        if (account.accountOperations) {
          account.accountOperations.forEach(operation => {
            if (operation.operationType === 'DEBIT') {
              debitCount++;
            } else if (operation.operationType === 'CREDIT') {
              creditCount++;
            }
          });
        }
      });
      this.renderDoughnutChart([debitCount, creditCount]);
    });
  }

  renderBarChart(labels: string[], data: number[]) {
    new Chart("barChart", {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: 'Accounts',
          data: data,
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  renderPieChart(data: number[]) {
    new Chart("pieChart", {
      type: 'pie',
      data: {
        labels: ['Current Accounts', 'Savings Accounts'],
        datasets: [{
          data: data,
          backgroundColor: ['#FF6384', '#36A2EB'],
          hoverOffset: 4
        }]
      },
      options: {
        responsive: true
      }
    });
  }

  renderDoughnutChart(data: number[]) {
    new Chart("doughnutChart", {
      type: 'doughnut',
      data: {
        labels: ['Debit', 'Credit'],
        datasets: [{
          data: data,
          backgroundColor: ['#FF6384', '#36A2EB'],
          hoverOffset: 4
        }]
      },
      options: {
        responsive: true
      }
    });
  }

  renderLineChart(labels: string[], data: number[]) {
    new Chart("lineChart", {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Balance',
          data: data,
          backgroundColor: 'rgba(153, 102, 255, 0.2)',
          borderColor: 'rgba(153, 102, 255, 1)',
          borderWidth: 1,
          fill: false
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
}
