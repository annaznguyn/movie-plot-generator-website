import React, { useState, useEffect } from "react";

import Header from "../components/dashboard/Navigation";
import MainSection, { DashboardCardInterface } from "../components/dashboard/MainSection";
import Footer from "../components/dashboard/Footer";

const Dashboard: React.FC = () => {
  const [stories, setStories] = useState<DashboardCardInterface[]>([]);
  const [genreFilterOption, setGenreFilterOption] = useState("Any");
  const [dateFilterOption, setDateFilterOption] = useState("Any");
  const [searchQuery, setSearchQuery] = useState("");

  const updateFilterOptions = (genre: string, date: string) => {
    setGenreFilterOption(genre);
    setDateFilterOption(date);
  }

  useEffect(() => {
    stories.forEach((story) => {
      // I wonder if this hook on this dependency forces a re-render?
    })
  }, [stories]);

  useEffect(() => {
    console.log("Dashboard.tsx --> Genre filter: ", genreFilterOption, "; Date filter: ", dateFilterOption);
  }, [genreFilterOption, dateFilterOption])

  return (
    <>
      <Header stories={stories} updateFilterOptions={updateFilterOptions} updateSearchQuery={setSearchQuery} />
      <MainSection setPropStories={setStories} dateFilterOption={dateFilterOption}
        genreFilterOption={genreFilterOption} searchQuery={searchQuery} />
      <Footer />
    </>
  );
}

export default Dashboard;