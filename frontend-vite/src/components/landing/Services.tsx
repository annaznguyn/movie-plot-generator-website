import React from "react";

export default function Services() {
    return (
        <section className="py-10 md:py-16">
            <div className="container">
                <div className="text-center">
                    <h2 className="text-3xl sm:text-5xl font-bold mb-4">What's in it for you?</h2>
                    <p className="text-lg sm:text-2xl mb-6 md:mb-14">
                        Craft stories that unfold in unique and captivating ways with the help of our
                        generative AI and story branching tool, inviting endless possibilities for exploration.
                    </p>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 lg:gap-8 xl:gap-10">
                    {[
                        {
                            icon: "bi-tree",
                            title: "Trees",
                            description: "A regular tree data structure with nodes to store the details of your\
                                 story like characters and important plot points where you could 'branch' off."
                        },
                        {
                            icon: "bi-robot",
                            title: "Gemini",
                            description: "Using Google's state-of-the-art generative AI as your co-captain on\
                                 your narrative journey at each tree node."
                        },
                        {
                            icon: "bi-floppy2-fill",
                            title: "Save",
                            description: "With our database solutions, you can save your stories and come back to\
                                    building your world for when you want to take a break from reality."
                        },
                        {
                            icon: "bi-file-ruled",
                            title: "Dashboard",
                            description: "Your own customized dashboard where you can easily manage your different\
                                    stories."
                        },
                        {
                            icon: "bi-wifi-off",
                            title: "Offline editing",
                            description: "You can work offline and edit your stories. However, your Gemini co-captain\
                                will not be available."
                        },
                        {
                            icon: "bi-pencil-square",
                            title: "Simple interface",
                            description: "Easy and simple editing interface that doesn't disrupt your workflow."
                        }
                    ].map((item, index) => (
                        <div key={index} className="card transform-gpu transition-all duration-200 hover:-translate-y-2 hover:shadow-lg bg-base-200">
                            <div className="card-body items-center text-center gap-4">
                                <i className={`bi ${item.icon} text-4xl`}></i>
                                <h2 className="card-title">{item.title}</h2>
                                <p>{item.description}<br className="hidden xl:inline" /></p>
                            </div>
                        </div>
                    ))}
                </div>
                <div>
                    <div className="flex items-center justify-center pt-16">
                        <button className="btn btn-secondary md:btn-lg group w-48 rounded-full px-12">Plans <i className="bi bi-arrow-right"></i></button>
                    </div>
                </div>
            </div>
        </section>

    )
}