

/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
$(document).ready(function() {
    $('body').hide().fadeIn(1000);
	$("a").click(function(e) {
	    e.preventDefault();
	    $link = $(this).attr("href");
	    $("body").fadeOut(1000,function(){
		    window.location =  $link; 
		});
    });
});


function handleStarResult(resultData) {
    console.log("handleStarResult: populating star table from resultData");

    // Populate the star table
    // Find the empty table body by id "star_table_body"
    let starTableBodyElement = jQuery("#movie_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        let num=i+1;
        rowHTML += "<th>" + num + "</th>";
        rowHTML +=
            "<th>" +
            // Add a link to single-star.html with id passed with GET url parameter
            '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
            + resultData[i]["movie_title"] +     // display star_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["genreList"] + "</th>";
        
        //For stars list
        let stars_name = resultData[i]["stars_name"].split(",");
        let stars_id = resultData[i]["stars_id"].split(",");
        rowHTML += "<th>";
        for (let j=0; j<stars_name.length-1; j++){
        	rowHTML+=('<a href="single-star.html?id=' + stars_id[j] + '">'
                    + stars_name[j] +     // display movie_name for the link text
                    '</a>'
        	);
        	rowHTML+= ",";
        }
        rowHTML = rowHTML.substring(0, rowHTML.length-1);
        rowHTML += "</th>";
        
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        starTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});