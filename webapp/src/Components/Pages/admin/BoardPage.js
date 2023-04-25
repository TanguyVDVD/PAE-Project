// eslint-disable-next-line import/no-extraneous-dependencies
import { Chart } from 'chart.js/auto';
import Navigate from '../../Router/Navigate';
import { getAuthenticatedUser } from '../../../utils/auths';
import { clearPage } from '../../../utils/render';
// eslint-disable-next-line no-unused-vars
import API from '../../../utils/api'

const BoardPage = () => {
    const authenticatedUser = getAuthenticatedUser();

    if (!authenticatedUser || authenticatedUser.role === null) {
      Navigate('/');
      return;
    }

  clearPage();
  renderBoardPage();
  renderBoardData();
};

function renderBoardPage(){
    const main = document.querySelector('main');
    const board = document.createElement('div');

    
    board.innerHTML=`
    <div class="container-fluid">

                    <!-- Page Heading -->
                    <div class="d-sm-flex align-items-center justify-content-between mb-4">
                    <div class="titreTab"> <h1 class="h3 mb-0 text-gray-800">Tableau de bord</h1></div>
                    </div>

                    <div class="row">

                        <!-- Area Chart -->
                        <div class="tab">
                            <div class="col-xl-10 col-lg-7">
                                <div class="card shadow mb-4" >
                                    <!-- Card Header - Dropdown -->
                                    <div
                                        class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                                        <h6 class="m-0 font-weight-bold text-primary">Suivi des objets</h6>
                                        <div class="dropdown no-arrow">
                                        </div>
                                    </div>

                                    <!-- Card Body -->
                                    <div class="card-body">
                                        <div id="chart-area">
                                            <label for="bday-month">Période:</label>
                                            <input id="bday-month" type="month" name="bday-month" value="2023-03">
                                            <br>

                                            <div class="dashboard">
                                                <div class="statistics">
                                                <br>
                                                    <h2>Statistiques</h2>
                                                    <ul>
                                                        <li>Nombre d'objets : <span id="totalObjects"></span></li>
                                                        <li>Nombre d'objets proposés : <span id="offeredObjects"></span></li>
                                                        <li>Nombre d'objets vendus : <span id="soldObjects"></span></li>
                                                        <li>Nombre d'objets acceptés : <span id="acceptedObjects"></span></li>
                                                        <li>Nombre d'objets refusés : <span id="rejectedObjects"></span></li>
                                                    </ul>
                                                </div>

                                                <div class="chart">
                                                <h2>Graphique</h2>
                                                    <canvas id="myChart"></canvas>
                                                </div> 
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </div>
                </div>
                <!-- /.container-fluid -->
    `;

    


    API.get(`objects/`).then((objects) => {
        const periode = document.getElementById('bday-month').value;

        const count = objects.length;
        document.getElementById('totalObjects').innerHTML =`
        ${count}
        ` ;

        const offers = objects.filter(object => object.state === 'proposé' );
        const countOffers= offers.length;
        document.getElementById('offeredObjects').innerHTML =`
        ${countOffers}
        ` ;

        const vendu = objects.filter(object => object.state === 'vendu' );
        // eslint-disable-next-line no-console
        console.log(vendu)
        const countSolds = vendu.length;
        document.getElementById('soldObjects').innerHTML =`
        ${countSolds}
        ` ;

        
        const acceptes = objects.filter(object => object.state === 'accepté' || object.state === 'en vente' || object.state ==="en magasin" );
        
        // eslint-disable-next-line no-console
        console.log(acceptes)
        const countAccep = acceptes.length;
        document.getElementById('acceptedObjects').innerHTML =`
        ${countAccep}
        ` ;

        const refuses = objects.filter(object => object.state === 'refusé' );
        // eslint-disable-next-line no-console
        console.log(refuses)
        const countRefuses = refuses.length;
        document.getElementById('rejectedObjects').innerHTML =`
        ${countRefuses}
        ` ;

        // eslint-disable-next-line no-unused-vars
        const proposedObjects = countOffers;
        const soldObjects = countSolds;
        const acceptedObjects = countAccep;
        const rejectedObjects = countRefuses;
        
    
        const chartElement = document.getElementById("myChart");
        
        
        // Création du graphique
        const chartData = {
          labels: [ "Proposés","Vendus", "Acceptés", "Refusés"],
          datasets: [
            {
              label: "Nombre d'objets",
              data: [proposedObjects ,soldObjects, acceptedObjects, rejectedObjects],
              backgroundColor: [
                "rgba(255, 99, 132, 0.2)",
                "rgba(54, 162, 235, 0.2)",
                "rgba(255, 206, 86, 0.2)",
                "rgba(75, 192, 192, 0.2)",
              ],
              borderColor: [
                "rgba(255, 99, 132, 1)",
                "rgba(54, 162, 235, 1)",
                "rgba(255, 206, 86, 1)",
                "rgba(75, 192, 192, 1)",
              
              ],
              borderWidth: 1,
            },
          ],
        };
        
        const chartOptions = {
          
        };

        // eslint-disable-next-line no-unused-vars
        const myChart = new Chart(chartElement, {
          type: 'pie',
          data: chartData,
          options: chartOptions,
        });
        


    });


    main.appendChild(board);
}

function renderBoardData(){

        
    
    }

export default BoardPage;